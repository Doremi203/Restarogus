package org.amogus.restarogus.controllers

import jakarta.validation.Valid
import org.amogus.restarogus.models.MenuItem
import org.amogus.restarogus.requests.AddMenuItemRequest
import org.amogus.restarogus.requests.UpdateMenuItemRequest
import org.amogus.restarogus.responses.GetMenuItemResponse
import org.amogus.restarogus.services.interfaces.MenuItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/menuItems")
class MenuItemController(
    private val menuItemsService: MenuItemService,
) {
    @GetMapping
    fun getMenuItems(): ResponseEntity<List<GetMenuItemResponse>> {
        val menuItems = menuItemsService.getMenuItems()

        return ResponseEntity.ok(menuItems.map { menuItem ->
            GetMenuItemResponse(
                menuItem.id,
                menuItem.name,
                menuItem.price,
                menuItem.cookTimeInMinutes,
                menuItem.quantity,
                menuItem.inMenu
            )
        })
    }

    @GetMapping("/{id}")
    fun getMenuItem(@PathVariable id: Long): ResponseEntity<GetMenuItemResponse> {
        val menuItem = menuItemsService.getMenuItemById(id)

        return ResponseEntity.ok(
            GetMenuItemResponse(
                menuItem.id,
                menuItem.name,
                menuItem.price,
                menuItem.cookTimeInMinutes,
                menuItem.quantity,
                menuItem.inMenu
            )
        )
    }

    @PostMapping
    fun addMenuItem(@Valid @RequestBody request: AddMenuItemRequest): ResponseEntity<Long> {
        val menuItemId = menuItemsService.addMenuItem(
            MenuItem(
                request.name,
                request.price,
                request.cookTimeInMinutes,
                request.quantity,
                request.inMenu,
            )
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemId)
    }

    @PatchMapping("/{id}")
    fun updateMenuItem(@PathVariable id: Long, @Valid @RequestBody request: UpdateMenuItemRequest): ResponseEntity<Unit> {
        menuItemsService.updateMenuItem(
            id,
            MenuItem(
                request.name,
                request.price,
                request.cookTimeInMinutes,
                request.quantity,
                request.inMenu,
            )
        )

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun removeMenuItem(@PathVariable id: Long): ResponseEntity<Unit> {
        menuItemsService.removeMenuItem(id)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}