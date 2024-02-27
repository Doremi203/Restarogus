package org.amogus.restarogus.repositories.dto

data class OrderPositionDTO(
    val orderId: Long,
    val menuItemId: Long,
    val quantity: Int,
    val quantityDone: Int = 0,
    val id: Long = 0L,
)
