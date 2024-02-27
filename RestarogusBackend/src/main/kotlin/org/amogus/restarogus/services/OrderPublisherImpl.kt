package org.amogus.restarogus.services

import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.services.interfaces.OrderPublisher
import org.amogus.restarogus.services.interfaces.OrderSubscriber
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

    override fun notify(orderIds: List<Long>) {
        subscribers.forEach { it.update(orderIds) }
    }

    @Scheduled(fixedRate = 30000)
    private fun getCurrentPendingOrders() {
        val orders = orderRepository.getAll()
        val pendingOrders = orders.filter { it.status == OrderStatus.PENDING }
        val pendingOrderIds = pendingOrders.map { it.id }
        notify(pendingOrderIds)
    }
}

