package org.example.coupon

import coupon.Member
import coupon.MemberGrade
import coupon.MemberGrade.*
import org.example.coupon.Coupon.Companion.createFreeDelivery
import org.example.coupon.Coupon.Companion.createTenPercent
import java.util.*

object CouponFactory {

    private val strategies: Map<MemberGrade, List<(UUID) -> Coupon>> = mapOf(
        BRONZE to emptyList(),
        SILVER to listOf { memberId -> createFreeDelivery(memberId) },
        GOLD to listOf(
            { memberId -> createFreeDelivery(memberId) },
            { memberId -> createTenPercent(memberId) }
        ),
        PLATINUM to listOf(
            { memberId -> createFreeDelivery(memberId) },
            { memberId -> createTenPercent(memberId) }
        )
    )

    fun byMember(member: Member): List<Coupon> = strategies[member.grade()]
        ?.map { it(member.id()) }
        ?: emptyList();
}