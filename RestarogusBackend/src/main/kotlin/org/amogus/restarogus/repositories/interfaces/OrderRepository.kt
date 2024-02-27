package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.dto.OrderDTO

interface OrderRepository {
    fun add(order: OrderDTO): Long
    fun update(order: OrderDTO)
    fun remove(orderId: Long)
    fun getById(orderId: Long): OrderDTO
    fun getAll(): List<OrderDTO>
    fun updateOrderStatus(orderId: Long, status: OrderStatus)
}