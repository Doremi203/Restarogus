package org.amogus.restarogus.repositories.dto

data class ReviewDTO(
    val orderId: Long,
    val rating: Int,
    val comment: String,
    val id: Long = 0,
)
