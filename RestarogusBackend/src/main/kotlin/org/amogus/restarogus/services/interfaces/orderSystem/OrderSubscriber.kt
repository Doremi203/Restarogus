package org.amogus.restarogus.services.interfaces.orderSystem

import org.amogus.restarogus.models.Order

interface OrderSubscriber {
    fun update(orders: List<Order>)
}