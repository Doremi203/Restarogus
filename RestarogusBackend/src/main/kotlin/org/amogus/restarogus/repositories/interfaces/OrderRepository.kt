package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.models.Order
import org.amogus.restarogus.models.OrderStatus

interface OrderRepository {
    fun add(order: Order): Long
    fun update(order: Order)
    fun remove(orderId: Long)
    fun getById(orderId: Long): Order
    fun getAll(): List<Order>
    fun updateOrderStatus(orderId: Long, status: OrderStatus)
}