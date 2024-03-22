package org.amogus.restarogus.responses

import java.math.BigDecimal

data class GetMenuItemResponse (
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val cookTimeInMinutes: Int,
    val quantity: Int,
    val inMenu: Boolean
)
