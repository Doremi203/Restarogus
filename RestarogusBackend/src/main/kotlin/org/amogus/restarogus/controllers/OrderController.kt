package org.amogus.restarogus.controllers

import jakarta.validation.Valid
import org.amogus.restarogus.requests.OrderRequest
import org.amogus.restarogus.requests.ReviewRequest
import org.amogus.restarogus.responses.MenuPositionsResponse
import org.amogus.restarogus.services.interfaces.orderSystem.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping("/menu")
    fun getMenu(): ResponseEntity<List<MenuPositionsResponse>> {
        val menu = orderService.getMenu().map { menuItem ->
            MenuPositionsResponse(
                id = menuItem.id,
                name = menuItem.name,
                price = menuItem.price
            )
        }
        return ResponseEntity.ok(menu)
    }

    @PostMapping
    fun placeOrder(@Valid @RequestBody request: OrderRequest): ResponseEntity<Long> {
        val orderId = orderService.placeOrder(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId)
    }

    @PostMapping("/{orderId}/positions")
    fun addPositions(
        @PathVariable orderId: Long,
        @Valid @RequestBody request: OrderRequest,
    ): ResponseEntity<Unit> {
        orderService.addPositions(orderId, request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: Long): ResponseEntity<Unit> {
        orderService.cancelOrder(orderId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PostMapping("/{orderId}/pay")
    fun payOrder(@PathVariable orderId: Long): ResponseEntity<Unit> {
        orderService.payOrder(orderId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @GetMapping("/{orderId}/status")
    fun getOrderStatus(@PathVariable orderId: Long): ResponseEntity<String> {
        val status = orderService.getOrderStatus(orderId).name
        return ResponseEntity.ok(status)
    }

    @PostMapping("/{orderId}/reviews")
    fun addReview(
        @PathVariable orderId: Long,
        @Valid @RequestBody review: ReviewRequest,
    ): ResponseEntity<Unit> {
        orderService.addReview(orderId, review)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}