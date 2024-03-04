package org.amogus.restarogus.controllers

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
    @GetMapping("/{id}")
    fun getMenuItem(@PathVariable id: Long): ResponseEntity<GetMenuItemResponse> {
        val menuItem = menuItemsService.getMenuItemById(id)

        return ResponseEntity.ok(menuItem)
    }

    @PostMapping
    fun addMenuItem(@RequestBody request: AddMenuItemRequest): ResponseEntity<Long> {
        val menuItemId = menuItemsService.addMenuItem(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemId)
    }

    @PatchMapping("/{id}")
    fun updateMenuItem(@PathVariable id: Long, @RequestBody request: UpdateMenuItemRequest): ResponseEntity<Unit> {
        menuItemsService.updateMenuItem(id, request)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun removeMenuItem(@PathVariable id: Long): ResponseEntity<Unit> {
        menuItemsService.removeMenuItem(id)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}