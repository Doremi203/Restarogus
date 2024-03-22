package org.amogus.restarogus.services.interfaces

import org.amogus.restarogus.models.MenuItem

interface MenuItemService {
    fun addMenuItem(menuItem: MenuItem): Long
    fun removeMenuItem(id: Long)
    fun updateMenuItem(id: Long, menuItem: MenuItem)
    fun updateMenuItemQuantity(id: Long, quantity: Int)
    fun getMenuItemById(id: Long): MenuItem
    fun getMenuItems(): List<MenuItem>
}