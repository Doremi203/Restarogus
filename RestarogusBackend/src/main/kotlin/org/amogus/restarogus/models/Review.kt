package org.amogus.restarogus.models

data class Review(
    val orderId: Long,
    val rating: Int,
    val comment: String,
    val id: Long = 0,
)
