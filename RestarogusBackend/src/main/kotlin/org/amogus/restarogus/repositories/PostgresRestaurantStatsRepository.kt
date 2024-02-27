package org.amogus.restarogus.repositories

import org.amogus.restarogus.repositories.interfaces.RestaurantStatsRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class PostgresRestaurantStatsRepository(
    private val dataBase: JdbcTemplate
) : RestaurantStatsRepository {
    override fun updateRevenue(revenue: BigDecimal) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE restaurant_stats
                    SET revenue = ?
                    WHERE id = ?
                    """.trimIndent()
            )
            preparedStatement.setBigDecimal(1, revenue)
            preparedStatement.setInt(2, 1)
            preparedStatement
        }

        dataBase.update(preparedStatementCreator)
    }

    override fun getRevenue(): BigDecimal {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT revenue
                    FROM restaurant_stats
                    """.trimIndent()
            )
            preparedStatement
        }

        return dataBase.query(preparedStatementCreator) { rs, _ -> rs.getBigDecimal("revenue") }.first()
    }
}