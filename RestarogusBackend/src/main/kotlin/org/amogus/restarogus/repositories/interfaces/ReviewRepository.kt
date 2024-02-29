package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.repositories.dto.ReviewDTO

interface ReviewRepository {
    fun add(review: ReviewDTO)
    fun getAll(): List<ReviewDTO>
    fun getAllByOrderId(orderId: Long): List<ReviewDTO>
}