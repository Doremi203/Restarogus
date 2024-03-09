package org.amogus.restarogus.models

import java.math.BigDecimal

data class MenuItem(
    val name: String,
    val price: BigDecimal,
    val cookTimeInMinutes: Int,
    val quantity: Int,
    val inMenu: Boolean,
    val id: Long = 0L,
)
