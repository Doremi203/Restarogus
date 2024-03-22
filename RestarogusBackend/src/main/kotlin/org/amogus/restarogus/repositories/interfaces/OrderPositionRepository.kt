package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.models.OrderPosition

interface OrderPositionRepository {
    fun add(order: OrderPosition): Long
    fun remove(id: Long)
    fun update(orderPosition: OrderPosition)
    fun updateQuantityDone(id: Long, quantityDone: Int)
    fun getAll(): List<OrderPosition>
    fun getAllByOrderId(orderId: Long): List<OrderPosition>
}