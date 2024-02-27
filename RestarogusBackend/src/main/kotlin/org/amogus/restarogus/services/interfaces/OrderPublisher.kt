package org.amogus.restarogus.services.interfaces

interface OrderPublisher {
    fun subscribe(subscriber: OrderSubscriber)
    fun unsubscribe(subscriber: OrderSubscriber)
    fun notify(orderIds: List<Long>)
}