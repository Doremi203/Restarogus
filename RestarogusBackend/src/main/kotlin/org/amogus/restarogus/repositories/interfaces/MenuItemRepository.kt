package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.models.MenuItem

interface MenuItemRepository {
    fun add(item: MenuItem): Long
    fun remove(id: Long)
    fun update(item: MenuItem)
    fun updateInMenuStatus(id: Long, inMenu: Boolean)
    fun updateQuantity(id: Long, quantity: Int)
    fun getById(id: Long): MenuItem
    fun getAll(): List<MenuItem>
}