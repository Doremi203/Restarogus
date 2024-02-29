package org.amogus.restarogus.controllers

import org.amogus.restarogus.repositories.interfaces.UserRepository
import org.amogus.restarogus.requests.OrderRequest
import org.amogus.restarogus.requests.ReviewRequest
import org.amogus.restarogus.services.interfaces.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val userRepository: UserRepository,
    private val orderService: OrderService
) {
    @PostMapping
    fun placeOrder(@RequestBody request: OrderRequest, principal: Principal): ResponseEntity<Long> {
        val customer = userRepository.getByUserName(principal.name)
            ?: return ResponseEntity.badRequest().build()

        val orderId = orderService.placeOrder(customer.id, request)
        return ResponseEntity.ok(orderId)
    }

    @PostMapping("/{orderId}/positions")
    fun addPositions(
        @PathVariable orderId: Long,
        @RequestBody request: OrderRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        val customer = userRepository.getByUserName(principal.name)
            ?: return ResponseEntity.badRequest().build()

        orderService.addPositions(customer.id, orderId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: Long, principal: Principal): ResponseEntity<Unit> {
        val customer = userRepository.getByUserName(principal.name)
            ?: return ResponseEntity.badRequest().build()

        orderService.cancelOrder(customer.id, orderId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{orderId}/pay")
    fun payOrder(@PathVariable orderId: Long, principal: Principal): ResponseEntity<Unit> {
        val customer = userRepository.getByUserName(principal.name)
            ?: return ResponseEntity.badRequest().build()

        orderService.payOrder(customer.id, orderId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{orderId}/status")
    fun getOrderStatus(@PathVariable orderId: Long, principal: Principal): ResponseEntity<String> {
        val customer = userRepository.getByUserName(principal.name)
            ?: return ResponseEntity.badRequest().build()

        val status = orderService.getOrderStatus(customer.id, orderId).name
        return ResponseEntity.ok(status)
    }

    @PostMapping("/{orderId}/reviews")
    fun addReview(
        @PathVariable orderId: Long,
        @RequestBody review: ReviewRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        val customer = userRepository.getByUserName(principal.name)
            ?: return ResponseEntity.badRequest().build()

        orderService.addReview(customer.id, orderId, review)
        return ResponseEntity.ok().build()
    }
}