package org.amogus.restarogus.requests

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class AddMenuItemRequest(
    @get:NotBlank(message = "Name is required")
    val name: String,
    @get:Min(0, message = "Price must be greater than or equal to 0")
    val price: BigDecimal,
    @get:Min(1, message = "Preparation time must be min 1")
    val cookTimeInMinutes: Int,
    @get:Min(1, message = "Quantity must be min 1")
    val quantity: Int,
    val inMenu: Boolean
)
