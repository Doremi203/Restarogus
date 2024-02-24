package org.amogus.restarogus.requests

import java.math.BigDecimal

data class UpdateMenuItemRequest(
    val name: String,
    val price: BigDecimal,
    val cookTimeInMinutes: Int,
    val quantity: Int
)
