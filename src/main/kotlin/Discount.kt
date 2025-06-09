package org.example

import DiscountStrategy
import Priority
import java.time.LocalDateTime

class Discount private constructor(
    val strategy: DiscountStrategy,
    val rateOrFixed: Double,
    val priority: Priority,
    val start: LocalDateTime,
    val end: LocalDateTime
) {
    companion object {
        operator fun invoke(strategy: DiscountStrategy, rateOrFixed: Double): Discount =
            Discount(strategy, rateOrFixed, strategy.priority(), LocalDateTime.MIN, LocalDateTime.MAX)

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

    private fun valid(target: LocalDateTime): Boolean = start.isBefore(target) && end.isBefore(target)

    private fun Double.nonNegative(): Double = this.coerceAtLeast(0.0)
}

