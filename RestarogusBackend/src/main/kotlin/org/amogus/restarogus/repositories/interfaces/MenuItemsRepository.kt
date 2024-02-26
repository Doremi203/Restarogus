package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.repositories.dto.MenuItemDTO

interface MenuItemsRepository {
    fun add(item: MenuItemDTO): Long
    fun remove(id: Long)
    fun update(item: MenuItemDTO)
    fun getById(id: Long): MenuItemDTO
}