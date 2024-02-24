package org.amogus.restarogus.services

import org.amogus.restarogus.repositories.dto.MenuItemDTO
import org.amogus.restarogus.repositories.interfaces.MenuItemsRepository
import org.amogus.restarogus.requests.AddMenuItemRequest
import org.amogus.restarogus.requests.UpdateMenuItemRequest
import org.amogus.restarogus.responses.GetMenuItemResponse
import org.amogus.restarogus.services.interfaces.MenuItemsService
import org.springframework.stereotype.Service

@Service
class MenuItemsServiceImpl(
    private val menuItemsRepository: MenuItemsRepository
) : MenuItemsService {
    override fun addMenuItem(menuItem: AddMenuItemRequest): Long {
        return menuItemsRepository.addMenuItem(
            MenuItemDTO(
                menuItem.name,
                menuItem.price,
                menuItem.cookTimeInMinutes,
                menuItem.quantity
            )
        )
    }

    override fun removeMenuItem(id: Long) {
        menuItemsRepository.removeMenuItem(id)
    }

    override fun updateMenuItem(id: Long, menuItem: UpdateMenuItemRequest) {
        menuItemsRepository.updateMenuItem(
            id,
            MenuItemDTO(
                menuItem.name,
                menuItem.price,
                menuItem.cookTimeInMinutes,
                menuItem.quantity
            )
        )
    }

    override fun getMenuItemById(id: Long): GetMenuItemResponse {
        val menuItem = menuItemsRepository.getMenuItemById(id)
        return GetMenuItemResponse(
            menuItem.id,
            menuItem.name,
            menuItem.price,
            menuItem.cookTimeInMinutes,
            menuItem.quantity
        )
    }
}