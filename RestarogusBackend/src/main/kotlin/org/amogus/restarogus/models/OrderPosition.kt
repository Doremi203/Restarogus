package org.amogus.restarogus.models

data class OrderPosition(
    val id: Long,
    val orderId: Long,
    val menuItemId: Long,
    val quantity: Int,
    val quantityDone: Int
)
