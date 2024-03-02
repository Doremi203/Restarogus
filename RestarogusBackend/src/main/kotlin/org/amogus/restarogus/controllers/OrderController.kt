package org.amogus.restarogus.controllers

import org.amogus.restarogus.models.User
import org.amogus.restarogus.requests.OrderRequest
import org.amogus.restarogus.requests.ReviewRequest
import org.amogus.restarogus.services.interfaces.orderSystem.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val userDetailsService: UserDetailsService,
    private val orderService: OrderService
) {
    @PostMapping
    fun placeOrder(@RequestBody request: OrderRequest, principal: Principal): ResponseEntity<Long> {
        val customer = userDetailsService.loadUserByUsername(principal.name) as User

        val orderId = orderService.placeOrder(customer.id, request)
        return ResponseEntity.ok(orderId)
    }

    @PostMapping("/{orderId}/positions")
    fun addPositions(
        @PathVariable orderId: Long,
        @RequestBody request: OrderRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        val customer = userDetailsService.loadUserByUsername(principal.name) as User

        orderService.addPositions(customer.id, orderId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: Long, principal: Principal): ResponseEntity<Unit> {
        val customer = userDetailsService.loadUserByUsername(principal.name) as User

        orderService.cancelOrder(customer.id, orderId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{orderId}/pay")
    fun payOrder(@PathVariable orderId: Long, principal: Principal): ResponseEntity<Unit> {
        val customer = userDetailsService.loadUserByUsername(principal.name) as User

        orderService.payOrder(customer.id, orderId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{orderId}/status")
    fun getOrderStatus(@PathVariable orderId: Long, principal: Principal): ResponseEntity<String> {
        val customer = userDetailsService.loadUserByUsername(principal.name) as User

        val status = orderService.getOrderStatus(customer.id, orderId).name
        return ResponseEntity.ok(status)
    }

    @PostMapping("/{orderId}/reviews")
    fun addReview(
        @PathVariable orderId: Long,
        @RequestBody review: ReviewRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        val customer = userDetailsService.loadUserByUsername(principal.name) as User

        orderService.addReview(customer.id, orderId, review)
        return ResponseEntity.ok().build()
    }
}