package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.repositories.dto.MenuItemDTO

interface MenuItemsRepository {
    fun addMenuItem(item: MenuItemDTO): Long
    fun removeMenuItem(id: Long)
    fun updateMenuItem(id: Long, item: MenuItemDTO)
    fun getMenuItemById(id: Long): MenuItemDTO
}