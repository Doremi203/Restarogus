package org.amogus.restarogus.repositories

import org.amogus.restarogus.models.Review
import org.amogus.restarogus.repositories.interfaces.ReviewRepository
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.stereotype.Repository

@Repository
class PostgresReviewRepository(
    private val dataBase: JdbcTemplate
) : ReviewRepository {
    override fun add(review: Review) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                "INSERT INTO reviews (order_id, rating, comment) VALUES (?, ?, ?)"
            )
            preparedStatement.setLong(1, review.orderId)
            preparedStatement.setInt(2, review.rating)
            preparedStatement.setString(3, review.comment)
            preparedStatement
        }

        dataBase.update(preparedStatementCreator)
    }

    override fun getAll(): List<Review> {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                "SELECT * FROM reviews"
            )
            preparedStatement
        }

        return dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(Review::class.java)
        )
    }

    override fun getAllByOrderId(orderId: Long): List<Review> {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                "SELECT * FROM reviews WHERE order_id = ?"
            )
            preparedStatement.setLong(1, orderId)
            preparedStatement
        }

        return dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(Review::class.java)
        )
    }
}