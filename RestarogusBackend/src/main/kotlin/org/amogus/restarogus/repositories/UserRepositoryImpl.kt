package org.amogus.restarogus.repositories

import org.amogus.restarogus.entities.Role
import org.amogus.restarogus.entities.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepositoryImpl(
    private val dataBase: JdbcTemplate
) : UserRepository {
    override fun add(user: User) {
        val sqlQuery = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)"
        dataBase.update(sqlQuery, user.id, user.userName, user.authPassword, user.role.name)
    }

    override fun getByUserName(username: String): User? {
        val sqlQuery = "SELECT * FROM users WHERE username = ?"
        val user = dataBase.query(sqlQuery, username) { response, _ ->
            User(
                UUID.fromString(response.getString("id")),
                response.getString("username"),
                response.getString("password"),
                Role.valueOf(response.getString("role"))
            )
        }

        return user.firstOrNull()
    }
}