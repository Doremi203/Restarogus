package org.amogus.restarogus.responses

import java.math.BigDecimal

data class MenuPositionsResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal,
)
