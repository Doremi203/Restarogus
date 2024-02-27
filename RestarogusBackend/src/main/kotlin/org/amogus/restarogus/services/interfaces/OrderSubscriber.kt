package org.amogus.restarogus.services.interfaces

interface OrderSubscriber {
    fun update(orderIds: List<Long>)
}