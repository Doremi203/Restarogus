package org.amogus.restarogus.repositories

import org.amogus.restarogus.models.User
import org.amogus.restarogus.repositories.interfaces.UserRepository
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class PostgresUserRepository(
    private val dataBase: JdbcTemplate
) : UserRepository {
    override fun add(user: User): Long {
        val keyHolder = GeneratedKeyHolder()

        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """INSERT INTO users(username, password, role) 
                    VALUES (?, ?, ?)""".trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            preparedStatement.setString(1, user.username)
            preparedStatement.setString(2, user.password)
            preparedStatement.setString(3, user.role.name)

            preparedStatement
        }
        dataBase.update(preparedStatementCreator, keyHolder)

        return keyHolder.keys!!["id"] as Long
    }

    override fun getByUserName(username: String): User {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT id, username, password, role FROM users WHERE username = ?
                """.trimIndent()
            )
            preparedStatement.setString(1, username)
            preparedStatement
        }

        val user = dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(User::class.java)
        )
            .firstOrNull()
            ?: throw NoSuchElementException("User with username $username does not exist")

        return user
    }
}