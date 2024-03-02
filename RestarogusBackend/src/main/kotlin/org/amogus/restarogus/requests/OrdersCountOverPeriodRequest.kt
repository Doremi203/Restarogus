package org.amogus.restarogus.requests

import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDateTime

data class OrdersCountOverPeriodRequest(
    @get:PastOrPresent
    val from: LocalDateTime,
    @get:PastOrPresent
    val to: LocalDateTime
)
