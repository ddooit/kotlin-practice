package org.example.coupon

import org.example.java.member.Member
import org.example.java.member.MemberGrade
import org.example.java.member.MemberGrade.*
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
        ?: emptyList()
}