package org.amogus.restarogus.repositories.dto

import org.amogus.restarogus.models.OrderStatus

data class OrderDTO(
    val customerId: Long,
    val status: OrderStatus,
    val id: Long = 0L,
)
