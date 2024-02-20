package org.amogus.restarogus.responses

data class GetMenuItemResponse (
    val id: Int,
    val name: String,
    val price: Double,
    val cookTimeInMinutes: Int,
    val quantity: Int,
)
