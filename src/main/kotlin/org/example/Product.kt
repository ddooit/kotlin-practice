package org.example

import java.time.Instant

class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val deliveryFee: Double = 0.0,
    val discountList: List<DiscountCondition>
) {
    fun discountedPrice(target: Instant): Double {
        val maxPriority = discountList
            .filter { it.valid(target) }
            .maxWith(DiscountCondition.comparator).getPriority()

        return discountList.filter { it.getPriority() == maxPriority }
            .minOfOrNull { it.getPrice(this@Product) }
            ?: 0.0
    }

}

