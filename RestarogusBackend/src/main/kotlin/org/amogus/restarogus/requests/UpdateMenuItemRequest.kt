package org.amogus.restarogus.requests

data class UpdateMenuItemRequest(
    val id: Int?,
    val name: String?,
    val price: Double?,
    val cookTimeInMinutes: Int?,
    val quantity: Int?
)
