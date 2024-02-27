package org.amogus.restarogus.requests

data class OrderRequest(
    val menuItems: List<MenuItemOrder>
) {
    data class MenuItemOrder(
        val id: Long,
        val quantity: Int
    )
}
