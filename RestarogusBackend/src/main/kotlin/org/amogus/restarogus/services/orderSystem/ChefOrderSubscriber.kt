package org.amogus.restarogus.services.orderSystem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.amogus.restarogus.models.Order
import org.amogus.restarogus.models.OrderPosition
import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.dto.OrderPositionDTO
import org.amogus.restarogus.repositories.interfaces.MenuItemRepository
import org.amogus.restarogus.repositories.interfaces.OrderPositionRepository
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.services.interfaces.orderSystem.OrderPublisher
import org.amogus.restarogus.services.interfaces.orderSystem.OrderSubscriber
import org.amogus.restarogus.services.interfaces.orderSystem.PriorityStrategy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

@Service
final class ChefOrderSubscriber(
    private val orderRepository: OrderRepository,
    private val orderPositionRepository: OrderPositionRepository,
    private val menuItemsRepository: MenuItemRepository,
    private val priorityStrategy: PriorityStrategy,
    orderPublisher: OrderPublisher,
) : OrderSubscriber {
    init {
        orderPublisher.subscribe(this)
    }

    private val cookersCount = 3
    private var currentCookingOrdersCount = AtomicInteger(0)
    private val logger = LoggerFactory.getLogger(ChefOrderSubscriber::class.java)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun update(orders: List<Order>) {
        val prioritizedOrders = priorityStrategy.getPriorityOrders(orders)
        prioritizedOrders.forEach { order ->
            if (currentCookingOrdersCount.get() < cookersCount) {
                coroutineScope.launch { cookOrder(order.id) }
                currentCookingOrdersCount.incrementAndGet()
            }
        }
    }

    private suspend fun cookOrder(orderId: Long) {
        var orderFinished = false
        orderRepository.updateOrderStatus(orderId, OrderStatus.IN_PROGRESS)
        logger.info("Cooking order $orderId")
        while (!orderFinished) {
            val order = orderRepository.getById(orderId)
            if (!isOrderNeededToProcess(order.status)) {
                break
            }

            val orderPositionToCook = orderPositionRepository.getAllByOrderId(orderId)
                .firstOrNull { it.quantityDone < it.quantity }
            if (orderPositionToCook != null) {
                cookOrderPosition(
                    OrderPosition(
                        orderPositionToCook.id,
                        orderPositionToCook.orderId,
                        orderPositionToCook.menuItemId,
                        orderPositionToCook.quantity,
                        orderPositionToCook.quantityDone
                    )
                )
            }

            orderFinished = checkOrderDone(orderId)
        }
        currentCookingOrdersCount.decrementAndGet()
    }

    private fun checkOrderDone(orderId: Long): Boolean {
        val positionsLeft = orderPositionRepository.getAllByOrderId(orderId).count {
            it.quantityDone < it.quantity
        }
        if (positionsLeft == 0) {
            orderRepository.updateOrderStatus(orderId, OrderStatus.FINISHED)
            return true
        }
        return false
    }

    private suspend fun cookOrderPosition(position: OrderPosition) {
        val menuItem = menuItemsRepository.getById(position.menuItemId)
        val toCookCount = position.quantity - position.quantityDone
        repeat(toCookCount) {
            delay(menuItem.cookTimeInMinutes * 1000L * 60)
            orderPositionRepository.update(
                OrderPositionDTO(
                    position.orderId,
                    position.menuItemId,
                    position.quantity,
                    position.quantityDone + 1,
                    position.id
                )
            )
        }
    }

    private fun isOrderNeededToProcess(orderStatus: OrderStatus) =
        orderStatus != OrderStatus.FINISHED && orderStatus != OrderStatus.CANCELLED
}