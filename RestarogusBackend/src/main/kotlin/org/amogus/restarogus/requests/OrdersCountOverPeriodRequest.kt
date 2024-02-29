package org.amogus.restarogus.requests

import java.time.LocalDateTime

data class OrdersCountOverPeriodRequest(
    val from: LocalDateTime,
    val to: LocalDateTime
)
