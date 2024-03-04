package org.amogus.restarogus.requests

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class ReviewRequest(
    @get:Min(1)
    @get:Max(5)
    val rating: Int,
    @get:NotBlank
    val comment: String
)
