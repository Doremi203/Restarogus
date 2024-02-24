package org.amogus.restarogus.repositories.dto

import java.math.BigDecimal

data class MenuItemDTO(
    val name: String,
    val price: BigDecimal,
    val cookTimeInMinutes: Int,
    val quantity: Int,
    val id: Long = 0L,
)
