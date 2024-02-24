package org.amogus.restarogus.services.interfaces

import org.amogus.restarogus.requests.AddMenuItemRequest
import org.amogus.restarogus.requests.UpdateMenuItemRequest
import org.amogus.restarogus.responses.GetMenuItemResponse

interface MenuItemsService {
    fun addMenuItem(menuItem: AddMenuItemRequest): Long
    fun removeMenuItem(id: Long)
    fun updateMenuItem(id: Long, menuItem: UpdateMenuItemRequest)
    fun getMenuItemById(id: Long): GetMenuItemResponse
}