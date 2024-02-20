package org.amogus.restarogus.entities

data class MenuItemUpdateEntity(
    val name: String?,
    val price: Double?,
    val cookTimeInMinutes: Int?,
    val quantity: Int?
)
