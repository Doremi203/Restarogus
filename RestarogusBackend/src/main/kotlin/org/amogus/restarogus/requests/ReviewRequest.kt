package org.amogus.restarogus.requests

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class ReviewRequest(
    @get:Min(1, message = "Rating must be between 1 and 5")
    @get:Max(5, message = "Rating must be between 1 and 5")
    val rating: Int,
    @get:NotBlank(message = "Comment is required")
    val comment: String
)
