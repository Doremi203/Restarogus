package org.amogus.restarogus.repositories.dto

import org.amogus.restarogus.models.OrderStatus
import java.time.LocalDateTime

data class OrderDTO(
    val customerId: Long,
    val status: OrderStatus,
    val date: LocalDateTime,
    val id: Long = 0L,
)
