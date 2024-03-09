package org.amogus.restarogus.services.orderSystem

import org.amogus.restarogus.models.Order
import org.amogus.restarogus.services.interfaces.orderSystem.PriorityStrategy

class OlderOrdersFirstPriorityStrategy : PriorityStrategy {
    override fun getPriorityOrders(orders: List<Order>): List<Order> {
        return orders.sortedBy { it.date }
    }
}