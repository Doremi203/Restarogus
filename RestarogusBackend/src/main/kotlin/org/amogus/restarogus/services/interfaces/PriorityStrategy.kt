package org.amogus.restarogus.services.interfaces

import org.amogus.restarogus.models.Order

interface PriorityStrategy {
    fun getPriorityOrders(orders: List<Order>): List<Order>
}
