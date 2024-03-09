package org.amogus.restarogus.models

data class OrderPosition(
    val orderId: Long,
    val menuItemId: Long,
    val quantity: Int,
    val quantityDone: Int = 0,
    val id: Long = 0L,
)
