package org.amogus.restarogus.services.interfaces.orderSystem

import org.amogus.restarogus.models.Order

interface OrderPublisher {
    fun subscribe(subscriber: OrderSubscriber)
    fun unsubscribe(subscriber: OrderSubscriber)
    fun notify(orders: List<Order>)
}