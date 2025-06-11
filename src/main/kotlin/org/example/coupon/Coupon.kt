package org.example.coupon

import org.example.Product
import org.example.java.coupon.CouponStatus
import org.example.java.coupon.CouponType
import org.example.java.coupon.CouponType.FREE_DELIVERY
import org.example.java.coupon.CouponType.TEN_PERCENT
import java.util.UUID
import java.time.Instant
import java.time.temporal.ChronoUnit

class Coupon private constructor(
    val id: UUID = UUID.randomUUID(),
    val memberId: UUID,
    val type: CouponType,
    val issuedAt: Instant = Instant.now(),
    val validUntil: Instant = issuedAt.plus(7L, ChronoUnit.DAYS),
    val status: CouponStatus = CouponStatus.ISSUED,
    val code: String,
    val description: String,
    val usedAt: Instant? = null
) {
    companion object {
        fun createFreeDelivery(memberId: UUID): Coupon = Coupon(
            memberId = memberId,
            type = FREE_DELIVERY,
            code = generateCode(),
            description = "무료배송"
        )

        fun createTenPercent(memberId: UUID): Coupon = Coupon(
            memberId = memberId,
            type = TEN_PERCENT,
            code = generateCode(),
            description = "10% 할인"
        )

        private fun generateCode(): String = ""
    }

    fun apply(product: Product): Double{
        return product.price
    }
}