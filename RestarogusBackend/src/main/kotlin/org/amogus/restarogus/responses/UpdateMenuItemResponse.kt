package org.amogus.restarogus.responses

data class UpdateMenuItemResponse(
    val message: String,
    val id: Int,
    val name: String,
    val price: Double,
    val cookTimeInMinutes: Int,
    val quantity: Int
)
