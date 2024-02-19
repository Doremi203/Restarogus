package org.amogus.restarogus.repositories

import org.amogus.restarogus.entities.User

interface UserRepository {
    fun add(user: User)
    fun getByUserName(username: String): User?
}