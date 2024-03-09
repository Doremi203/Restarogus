package org.amogus.restarogus.requests

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class UpdateMenuItemRequest(
    @get:NotBlank
    val name: String,
    @get:Min(0)
    val price: BigDecimal,
    @get:Min(1)
    val cookTimeInMinutes: Int,
    @get:Min(1)
    val quantity: Int,
    val inMenu: Boolean
)
