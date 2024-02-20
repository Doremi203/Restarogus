package org.amogus.restarogus.repositories

import org.amogus.restarogus.entities.MenuItemEntity
import org.amogus.restarogus.entities.MenuItemUpdateEntity
import org.amogus.restarogus.models.MenuItem

interface MenuRepository {
    fun addMenuItem(item: MenuItemEntity): Int
    fun removeMenuItem(id: Int)
    fun updateMenuItem(id: Int, item: MenuItemUpdateEntity)

    fun getMenuItem(id: Int): MenuItem
}