package org.example.coupon

import coupon.CouponStatus
import coupon.CouponType
import java.util.UUID
import java.time.Instant
import java.time.temporal.ChronoUnit

class Coupon(
    val id: UUID = UUID.randomUUID(),
    val memberId: UUID,
    val type: CouponType,
    val issuedAt: Instant = Instant.now(),
    val validUntil: Instant = issuedAt.plus(7L, ChronoUnit.DAYS),
    val status: CouponStatus = CouponStatus.ISSUED,
    val code: String,
    val description: String,
    val usedAt: Instant? = null
)