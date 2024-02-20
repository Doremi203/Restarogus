package org.amogus.restarogus.models

data class MenuItem(
    val id: Int,
    val name: String,
    val price: Double,
    val cookTimeInMinutes: Int,
    val quantity: Int,
)
