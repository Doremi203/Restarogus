package org.amogus.restarogus.requests

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

data class OrderRequest(
    @get:NotEmpty
    val menuItems: List<MenuItemOrder>
) {
    data class MenuItemOrder(
        val id: Long,
        @get:Min(1)
        val quantity: Int
    )
}
