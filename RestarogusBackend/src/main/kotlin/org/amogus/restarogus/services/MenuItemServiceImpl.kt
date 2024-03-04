package org.amogus.restarogus.services

import org.amogus.restarogus.repositories.dto.MenuItemDTO
import org.amogus.restarogus.repositories.interfaces.MenuItemRepository
import org.amogus.restarogus.requests.AddMenuItemRequest
import org.amogus.restarogus.requests.UpdateMenuItemRequest
import org.amogus.restarogus.responses.GetMenuItemResponse
import org.amogus.restarogus.services.interfaces.MenuItemService
import org.springframework.stereotype.Service

@Service
class MenuItemServiceImpl(
    private val menuItemsRepository: MenuItemRepository
) : MenuItemService {
    override fun addMenuItem(menuItem: AddMenuItemRequest): Long {
        return menuItemsRepository.add(
            MenuItemDTO(
                menuItem.name,
                menuItem.price,
                menuItem.cookTimeInMinutes,
                menuItem.quantity
            )
        )
    }

    override fun removeMenuItem(id: Long) {
        menuItemsRepository.remove(id)
    }

    override fun updateMenuItem(id: Long, menuItem: UpdateMenuItemRequest) {
        menuItemsRepository.update(
            MenuItemDTO(
                menuItem.name,
                menuItem.price,
                menuItem.cookTimeInMinutes,
                menuItem.quantity,
                id
            )
        )
    }

    override fun getMenuItemById(id: Long): GetMenuItemResponse {
        val menuItem = menuItemsRepository.getById(id)
        return GetMenuItemResponse(
            menuItem.id,
            menuItem.name,
            menuItem.price,
            menuItem.cookTimeInMinutes,
            menuItem.quantity
        )
    }
}