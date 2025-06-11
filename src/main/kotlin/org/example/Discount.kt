package org.example

import org.example.java.DiscountStrategy
import org.example.java.Priority
import java.time.LocalDateTime

class Discount private constructor(
    val strategy: DiscountStrategy,
    val rateOrFixed: Double,
    val priority: Priority,
    val start: LocalDateTime = LocalDateTime.MIN,
    val end: LocalDateTime = LocalDateTime.MAX
) {
    companion object {
        operator fun invoke(strategy: DiscountStrategy, rateOrFixed: Double): Discount =
            Discount(strategy = strategy, rateOrFixed = rateOrFixed, priority = strategy.priority())

        operator fun invoke(
            strategy: DiscountStrategy,
            rateOrFixed: Double,
            start: LocalDateTime,
            end: LocalDateTime
        ): Discount =
            Discount(strategy, rateOrFixed, strategy.priorityWithPeriod(), start, end)
    }

    fun getPriority(target: LocalDateTime): Priority {
        if (valid(target)) {
            return priority
        }
        return Priority.LAST
    }

    fun getPrice(price: Double, target: LocalDateTime): Double {
        if (!valid(target)) {
            return Double.MAX_VALUE
        }
        return strategy.calculate(price, rateOrFixed).nonNegative()
    }

    private fun valid(target: LocalDateTime): Boolean =
        start.isBefore(target) && end.isAfter(target)

    private fun Double.nonNegative(): Double = this.coerceAtLeast(0.0)
}