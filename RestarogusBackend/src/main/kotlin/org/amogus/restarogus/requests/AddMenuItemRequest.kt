package org.amogus.restarogus.requests

data class AddMenuItemRequest(
    val name: String,
    val price: Double,
    val cookTimeInMinutes: Int,
    val quantity: Int,
)
