package org.amogus.restarogus.models

import java.time.LocalDateTime

data class Order(
    val customerId: Long,
    val status: OrderStatus,
    val date: LocalDateTime,
    val id: Long = 0L,
)
