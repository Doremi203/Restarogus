package org.amogus.restarogus.validators

import org.amogus.restarogus.exceptions.IncorrectOrderStatusException
import org.amogus.restarogus.exceptions.MenuItemNotInMenuException
import org.amogus.restarogus.models.Order
import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.models.Role
import org.amogus.restarogus.models.User
import org.amogus.restarogus.services.interfaces.MenuItemService
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DefaultOrderServiceValidator(
    private val menuItemService: MenuItemService
) : OrderServiceValidator {
    override fun assertOrderOwnership(order: Order) {
        val customer = getCurrentUser()

        if (order.customerId != customer.id) {
            throw AccessDeniedException("Order does not belong to the customer")
        }
    }

    override fun assertOrderOwnershipWithAdminAccess(order: Order) {
        val customer = getCurrentUser()

        if (order.customerId != customer.id && customer.role != Role.ADMIN) {
            throw AccessDeniedException("Order cannot be accessed by the user")
        }
    }

    override fun asserOrderPending(order: Order) {
        if (order.status == OrderStatus.FINISHED
            || order.status == OrderStatus.CANCELLED
            || order.status == OrderStatus.PAYED) {
            throw IncorrectOrderStatusException("Order is not processing")
        }
    }

    override fun assertOrderCanBePayed(order: Order) {
        if (order.status != OrderStatus.FINISHED) {
            throw IncorrectOrderStatusException("Order cannot be payed")
        }
    }

    override fun assertOrderCanBeReviewed(order: Order) {
        if (order.status != OrderStatus.PAYED) {
            throw IncorrectOrderStatusException("Order is not payed")
        }
    }

    override fun assertMenuItemInMenu(id: Long) {
        val menuItem = try {
            menuItemService.getMenuItemById(id)
        } catch (e: NoSuchElementException) {
            throw MenuItemNotInMenuException("Menu item is not in the menu")
        }

        if (!menuItem.inMenu) {
            throw MenuItemNotInMenuException("Menu item is not in the menu")
        }
    }

    private fun getCurrentUser(): User {
        return SecurityContextHolder.getContext().authentication.principal as User
    }
}