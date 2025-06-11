package org.example

import org.example.java.Priority
import java.time.LocalDateTime

class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val discountList: List<Discount>
) {
    fun discountedPrice(target: LocalDateTime): Double {
        val discountList = appliedDiscounts(target)
            .takeIf { it.isNotEmpty() }
            ?: this@Product.discountList

        return getMinPrice(target, discountList)
    }

    private fun appliedDiscounts(target: LocalDateTime): MutableList<Discount> {
        if (discountList.isEmpty()) {
            return mutableListOf()
        }

        val maxPriority = discountList
            .map { it.getPriority(target) }
            .maxWith (Priority.getComparator())

        return discountList
            .filter { it.getPriority(target) == maxPriority }
            .toMutableList()
    }

    private fun getMinPrice(target: LocalDateTime, discountList: List<Discount>): Double {
        return discountList.fold(price) { acc, discount ->
            minOf(
                acc,
                discount.getPrice(price, target))
        }
    }

}

