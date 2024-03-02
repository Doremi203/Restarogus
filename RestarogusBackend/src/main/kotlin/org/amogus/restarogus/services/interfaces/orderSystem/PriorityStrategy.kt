package org.amogus.restarogus.services.interfaces.orderSystem

import org.amogus.restarogus.models.Order

interface PriorityStrategy {
    fun getPriorityOrders(orders: List<Order>): List<Order>
}
