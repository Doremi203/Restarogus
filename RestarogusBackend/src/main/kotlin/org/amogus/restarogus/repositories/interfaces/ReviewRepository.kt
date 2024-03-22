package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.models.Review

interface ReviewRepository {
    fun add(review: Review)
    fun getAll(): List<Review>
    fun getAllByOrderId(orderId: Long): List<Review>
}