package org.amogus.restarogus.services

import org.amogus.restarogus.models.Order
import org.amogus.restarogus.services.interfaces.PriorityStrategy

class OlderOrdersFirstPriorityStrategy : PriorityStrategy {
    override fun getPriorityOrders(orders: List<Order>): List<Order> {
        return orders.sortedByDescending { it.dateTime }
    }
}