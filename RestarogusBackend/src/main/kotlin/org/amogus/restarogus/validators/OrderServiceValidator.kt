package org.amogus.restarogus.validators

import org.amogus.restarogus.models.Order

interface OrderServiceValidator {
    fun assertOrderOwnership(order: Order)
    fun assertOrderOwnershipWithAdminAccess(order: Order)
    fun asserOrderPending(order: Order)
    fun assertOrderCanBePayed(order: Order)
    fun assertOrderCanBeReviewed(order: Order)
    fun assertMenuItemInMenu(id: Long)
}
