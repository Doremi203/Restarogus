package org.amogus.restarogus.controllers

import org.amogus.restarogus.entities.MenuItemEntity
import org.amogus.restarogus.repositories.MenuRepository
import org.amogus.restarogus.requests.AddMenuItemRequest
import org.amogus.restarogus.responses.AddMenuItemResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/administration")
class AdminController(
    private val menuRepository: MenuRepository
) {
    @PostMapping("/addMenuItem")
    fun addMenuItem(@RequestBody request: AddMenuItemRequest): ResponseEntity<AddMenuItemResponse> {
        val menuItemId = menuRepository.addMenuItem(MenuItemEntity(
            name = request.name,
            price = request.price,
            cookTimeInMinutes = request.cookTimeInMinutes,
            quantity = request.quantity
        ))

        return ResponseEntity.ok(AddMenuItemResponse("Item added successfully", menuItemId))
    }
}