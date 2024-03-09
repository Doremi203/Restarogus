package org.amogus.restarogus.services.orderSystem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.amogus.restarogus.models.Order
import org.amogus.restarogus.models.OrderPosition
import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.interfaces.OrderPositionRepository
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.services.interfaces.MenuItemService
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
    private val menuItemsService: MenuItemService,
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
            if (!isOrderNeededToProcess(order.status))
                break

            processOneOrderPosition(orderId)

            orderFinished = checkOrderDone(orderId)
        }
        currentCookingOrdersCount.decrementAndGet()
    }

    private suspend fun processOneOrderPosition(orderId: Long) {
        val orderPositionToCook = orderPositionRepository.getAllByOrderId(orderId)
            .firstOrNull { it.quantityDone < it.quantity }
        if (orderPositionToCook != null)
            cookOrderPosition(orderPositionToCook)
    }

    private fun checkOrderDone(orderId: Long): Boolean {
        val positionsLeft = orderPositionRepository.getAllByOrderId(orderId).count {
            it.quantityDone < it.quantity
        }
        if (positionsLeft == 0) {
            orderRepository.updateOrderStatus(orderId, OrderStatus.FINISHED)
            logger.info("Order $orderId is finished")
            return true
        }
        return false
    }

    private suspend fun cookOrderPosition(position: OrderPosition) {
        val menuItem = menuItemsService.getMenuItemById(position.menuItemId)
        val toCookCount = position.quantity - position.quantityDone
        repeat(toCookCount) {
            if (!isMenuItemInStock(position))
                return

            delay(menuItem.cookTimeInMinutes * 1000L * 60)
            orderPositionRepository.updateQuantityDone(position.id, position.quantityDone + 1)
        }
    }

    private fun isMenuItemInStock(position: OrderPosition): Boolean {
        synchronized(this) {
            val curQuantity = menuItemsService.getMenuItemById(position.menuItemId).quantity
            if (curQuantity > 0) {
                menuItemsService.updateMenuItemQuantity(
                    position.menuItemId,
                    curQuantity - 1
                )
                return true
            } else {
                orderRepository.updateOrderStatus(position.orderId, OrderStatus.WAITING_RESTOCK)
                return false
            }
        }
    }

    private fun isOrderNeededToProcess(orderStatus: OrderStatus) =
        orderStatus != OrderStatus.FINISHED
                && orderStatus != OrderStatus.CANCELLED
                && orderStatus != OrderStatus.WAITING_RESTOCK
}