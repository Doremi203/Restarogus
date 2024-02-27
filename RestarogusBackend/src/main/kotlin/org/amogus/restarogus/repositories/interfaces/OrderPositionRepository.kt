package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.repositories.dto.OrderPositionDTO

interface OrderPositionRepository {
    fun add(order: OrderPositionDTO): Long
    fun remove(id: Long)
    fun update(orderPosition: OrderPositionDTO)
    fun getAllByOrderId(orderId: Long): List<OrderPositionDTO>
}