package org.example.coupon

import coupon.CouponType.FREE_DELIVERY
import coupon.CouponType.TEN_PERCENT
import coupon.Member
import coupon.MemberGrade

class CouponIssueService(
    val couponStore: CouponStore
) {

    fun getCoupons(member: Member): List<Coupon> {
        val issuedCouponTypeSet = couponStore.findIssuedCouponsByMemberId(member.id())
            .map { it.type }
            .toSet()

        val remainingCouponCountMap = couponStore.remainingCountMap()

        val list = getByMember(member)
            .filter { !issuedCouponTypeSet.contains(it.type) }
            .filter { remainingCouponCountMap[it.type]!! > 0 }
            .toList()

        return couponStore.issue(list)
    }

    private fun getByMember(member: Member): List<Coupon> = when (member.grade()) {
        MemberGrade.BRONZE ->
            emptyList()

        MemberGrade.SILVER ->
            listOf(
                Coupon(memberId = member.id(), type = FREE_DELIVERY, code = "DDDD", description = "무료배송")
            )

        MemberGrade.GOLD, MemberGrade.PLATINUM ->
            listOf(
                Coupon(memberId = member.id(), type = FREE_DELIVERY, code = "DDDD", description = "무료배송"),
                Coupon(memberId = member.id(), type = TEN_PERCENT, code = "DDDD", description = "10% 할인")
            )
    }
}