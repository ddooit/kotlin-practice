package org.example

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
            .maxBy { it.getPriority(target) }
            .getPriority(target)

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

