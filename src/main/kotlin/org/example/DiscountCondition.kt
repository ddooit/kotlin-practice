package org.example

import org.example.java.Priority
import java.time.Instant


sealed class DiscountCondition(
    open val period: Period? = null,
) {
    abstract fun getPrice(product: Product): Double

    abstract fun getPriority(): Priority

    fun valid(now: Instant): Boolean = period?.valid(now) ?: true

    data class Fixed(
        val amount: Double,
        override val period: Period? = null
    ) : DiscountCondition() {

        override fun getPrice(product: Product): Double =
            (product.price + product.deliveryFee - amount).coerceAtLeast(0.0)

        override fun getPriority(): Priority = Priority.SECOND

    }

    data class Rate(
        val rate: Double,
        override val period: Period? = null
    ) : DiscountCondition() {

        override fun getPrice(product: Product): Double =
            ((product.price + product.deliveryFee) * (1 - rate)).coerceAtLeast(0.0)

        override fun getPriority(): Priority = Priority.FIRST
    }

    class FreeDelivery : DiscountCondition() {
        override fun getPrice(product: Product): Double = product.price

        override fun getPriority(): Priority = Priority.LAST
    }

    companion object {
        val comparator: Comparator<DiscountCondition> =
            compareByDescending<DiscountCondition> { it.getPriority() }
    }


    class Period(
        val start: Instant,
        val end: Instant
    ) {
        fun valid(target: Instant): Boolean = target in start..end
    }

}