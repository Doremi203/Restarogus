package org.amogus.restarogus.services.interfaces

import org.amogus.restarogus.models.Order

interface OrderSubscriber {
    fun update(orders: List<Order>)
}