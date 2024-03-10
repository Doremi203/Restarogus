package org.amogus.restarogus.requests

import jakarta.validation.constraints.PastOrPresent
import org.amogus.restarogus.requests.validators.ValidTimeOrder
import java.time.LocalDateTime

@ValidTimeOrder
data class OrdersCountOverPeriodRequest(
    @get:PastOrPresent(message = "From must be in the past or present")
    val from: LocalDateTime,
    @get:PastOrPresent(message = "To must be in the past or present")
    val to: LocalDateTime
)
