package org.example.coupon

import org.example.java.coupon.CouponType
import java.util.UUID

interface CouponStore {

    fun findIssuedCouponsByMemberId(memberId: UUID): List<Coupon>

    fun remainingCountMap(): Map<CouponType, Int>

    fun issue(coupons: List<Coupon>): List<Coupon>
}