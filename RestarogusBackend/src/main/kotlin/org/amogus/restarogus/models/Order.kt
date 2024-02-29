package org.amogus.restarogus.models

import java.time.LocalDateTime

data class Order(
    val id: Long,
    val dateTime: LocalDateTime
)
