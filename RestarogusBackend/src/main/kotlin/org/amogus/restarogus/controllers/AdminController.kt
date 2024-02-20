package org.amogus.restarogus.controllers

import org.amogus.restarogus.entities.MenuItemEntity
import org.amogus.restarogus.entities.MenuItemUpdateEntity
import org.amogus.restarogus.repositories.MenuRepository
import org.amogus.restarogus.requests.AddMenuItemRequest
import org.amogus.restarogus.requests.DeleteMenuItemRequest
import org.amogus.restarogus.requests.GetMenuItemRequest
import org.amogus.restarogus.requests.UpdateMenuItemRequest
import org.amogus.restarogus.responses.AddMenuItemResponse
import org.amogus.restarogus.responses.DeleteMenuItemResponse
import org.amogus.restarogus.responses.GetMenuItemResponse
import org.amogus.restarogus.responses.UpdateMenuItemResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/administration")
class AdminController(
    private val menuRepository: MenuRepository
) {
    @GetMapping("/getMenuItem")
    fun getMenuItem(@RequestBody request: GetMenuItemRequest): ResponseEntity<GetMenuItemResponse> {
        val menuItem = menuRepository.getMenuItem(request.id)

        return ResponseEntity.ok(
            GetMenuItemResponse(
                menuItem.id,
                menuItem.name,
                menuItem.price,
                menuItem.cookTimeInMinutes,
                menuItem.quantity
            )
        )
    }

    @PostMapping("/addMenuItem")
    fun addMenuItem(@RequestBody request: AddMenuItemRequest): ResponseEntity<AddMenuItemResponse> {
        val menuItemId = menuRepository.addMenuItem(
            MenuItemEntity(
                name = request.name,
                price = request.price,
                cookTimeInMinutes = request.cookTimeInMinutes,
                quantity = request.quantity
            )
        )

        return ResponseEntity.ok(AddMenuItemResponse("Item added successfully", menuItemId))
    }

    @PatchMapping("/updateMenuItem")
    fun updateMenuItem(@RequestBody request: UpdateMenuItemRequest): ResponseEntity<UpdateMenuItemResponse> {
        if (request.id == null)
            return ResponseEntity.badRequest().build()

        menuRepository.updateMenuItem(
            request.id,
            MenuItemUpdateEntity(
                name = request.name,
                price = request.price,
                cookTimeInMinutes = request.cookTimeInMinutes,
                quantity = request.quantity
            )
        )

        val menuItem = menuRepository.getMenuItem(request.id)

        return ResponseEntity.ok(
            UpdateMenuItemResponse(
                "Item updated successfully",
                menuItem.id,
                menuItem.name,
                menuItem.price,
                menuItem.cookTimeInMinutes,
                menuItem.quantity
            )
        )
    }

    @DeleteMapping("/removeMenuItem")
    fun removeMenuItem(@RequestBody request: DeleteMenuItemRequest): ResponseEntity<DeleteMenuItemResponse> {
        menuRepository.removeMenuItem(request.id)
        return ResponseEntity.ok(DeleteMenuItemResponse("Item removed successfully", request.id))
    }
}