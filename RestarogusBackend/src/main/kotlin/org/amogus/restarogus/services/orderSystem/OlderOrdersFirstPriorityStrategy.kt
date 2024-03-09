package org.amogus.restarogus.services.orderSystem

import org.amogus.restarogus.models.Order
import org.amogus.restarogus.services.interfaces.orderSystem.OrderPriorityStrategy

class OlderOrdersFirstPriorityStrategy : OrderPriorityStrategy {
    override fun getPriorityOrders(orders: List<Order>): List<Order> {
        return orders.sortedBy { it.date }
    }
}