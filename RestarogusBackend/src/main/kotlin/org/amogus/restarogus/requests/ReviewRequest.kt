package org.amogus.restarogus.requests

data class ReviewRequest(
    val rating: Int,
    val comment: String
)
