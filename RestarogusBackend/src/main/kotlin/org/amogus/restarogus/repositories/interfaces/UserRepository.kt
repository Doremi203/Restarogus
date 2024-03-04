package org.amogus.restarogus.repositories.interfaces

import org.amogus.restarogus.models.User

interface UserRepository {
    fun add(user: User): Long
    fun getByUserName(username: String): User
}