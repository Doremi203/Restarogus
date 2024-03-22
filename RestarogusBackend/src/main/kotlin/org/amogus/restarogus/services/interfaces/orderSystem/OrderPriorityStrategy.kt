package org.amogus.restarogus.services.interfaces.orderSystem

import org.amogus.restarogus.models.Order

interface OrderPriorityStrategy {
    fun getPriorityOrders(orders: List<Order>): List<Order>
}
