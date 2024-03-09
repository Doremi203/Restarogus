package org.amogus.restarogus.services

import org.amogus.restarogus.models.MenuItem
import org.amogus.restarogus.repositories.interfaces.MenuItemRepository
import org.amogus.restarogus.services.interfaces.MenuItemService
import org.springframework.stereotype.Service

@Service
class MenuItemServiceImpl(
    private val menuItemsRepository: MenuItemRepository
) : MenuItemService {
    override fun addMenuItem(menuItem: MenuItem): Long {
        return menuItemsRepository.add(menuItem)
    }

    override fun removeMenuItem(id: Long) {
        menuItemsRepository.updateInMenuStatus(id, false)
    }

    override fun updateMenuItem(id: Long, menuItem: MenuItem) {
        menuItemsRepository.update(menuItem.copy(id = id))
    }

    override fun updateMenuItemQuantity(id: Long, quantity: Int) {
        menuItemsRepository.updateQuantity(id, quantity)
    }

    override fun getMenuItemById(id: Long): MenuItem {
        return menuItemsRepository.getById(id)
    }

    override fun getMenuItems(): List<MenuItem> {
        return menuItemsRepository.getAll()
    }
}