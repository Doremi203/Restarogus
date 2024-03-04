package org.amogus.restarogus.services.orderSystem

import org.amogus.restarogus.models.Order
import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.services.interfaces.orderSystem.OrderPublisher
import org.amogus.restarogus.services.interfaces.orderSystem.OrderSubscriber
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OrderPublisherImpl(
    private val orderRepository: OrderRepository
) : OrderPublisher {
    private val subscribers = mutableListOf<OrderSubscriber>()

    override fun subscribe(subscriber: OrderSubscriber) {
        subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: OrderSubscriber) {
        subscribers.remove(subscriber)
    }

    override fun notify(orders: List<Order>) {
        subscribers.forEach { it.update(orders) }
    }

    @Scheduled(fixedRate = 30000)
    private fun getCurrentPendingOrders() {
        val orders = orderRepository.getAll()
        val pendingOrders = orders.filter { it.status == OrderStatus.PENDING }
        notify(pendingOrders.map { Order(it.id, it.date) })
    }
}
