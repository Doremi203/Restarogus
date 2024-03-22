package org.amogus.restarogus.services.orderSystem

import org.amogus.restarogus.models.Order
import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.interfaces.OrderPositionRepository
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.services.interfaces.MenuItemService
import org.amogus.restarogus.services.interfaces.orderSystem.OrderPublisher
import org.amogus.restarogus.services.interfaces.orderSystem.OrderSubscriber
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OrderPublisherImpl(
    private val orderRepository: OrderRepository,
    private val orderPositionRepository: OrderPositionRepository,
    private val menuItemService: MenuItemService,
) : OrderPublisher {
    private val subscribers = mutableListOf<OrderSubscriber>()
    private val logger = LoggerFactory.getLogger(OrderPublisherImpl::class.java)

    init {
        processInterruptedOrders()
    }

    override fun subscribe(subscriber: OrderSubscriber) {
        subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: OrderSubscriber) {
        subscribers.remove(subscriber)
    }

    override fun notify(orders: List<Order>) {
        subscribers.forEach { it.update(orders) }
    }

    @Scheduled(fixedRate = 50000)
    private fun checkOrdersRestock() {
        val orders = orderRepository.getAll()
        val waitingForRestockOrders = orders.filter {
            it.status == OrderStatus.WAITING_RESTOCK
        }

        waitingForRestockOrders.forEach {
            val orderPositions = orderPositionRepository.getAllByOrderId(it.id)
            val isRestocked = orderPositions.all { orderPosition ->
                val menuItem = menuItemService.getMenuItemById(orderPosition.menuItemId)
                menuItem.quantity >= orderPosition.quantity
            }

            if (isRestocked) {
                orderRepository.updateOrderStatus(it.id, OrderStatus.PENDING)
            }
        }
    }

    @Scheduled(fixedRate = 30000)
    private fun getCurrentPendingOrders() {
        try {
            val orders = orderRepository.getAll()
            val pendingOrders = orders.filter {
                it.status == OrderStatus.PENDING
            }

            notify(pendingOrders)
        } catch (e: Exception) {
            logger.error("Error while processing pending orders", e)
        }
    }

    private fun processInterruptedOrders() {
        try {
            val orders = orderRepository.getAll()
            val interruptedOrders = orders.filter { it.status == OrderStatus.IN_PROGRESS }

            notify(interruptedOrders)
        } catch (e: Exception) {
            logger.error("Error while processing interrupted orders", e)
        }
    }
}

