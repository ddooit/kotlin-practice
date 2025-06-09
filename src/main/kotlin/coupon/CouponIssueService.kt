package org.example.coupon

import coupon.Member

class CouponIssueService(
    val couponStore: CouponStore
) {

    fun getCoupons(member: Member): List<Coupon> {
        val issuedCouponTypeSet = couponStore.findIssuedCouponsByMemberId(member.id())
            .map { it.type }
            .toSet()

        val remainingCouponCountMap = couponStore.remainingCountMap()

        val list = CouponFactory.byMember(member)
            .filter { !issuedCouponTypeSet.contains(it.type) }
            .filter { remainingCouponCountMap.getOrDefault(it.type, 0) > 0 }
            .toList()

        return couponStore.issue(list)
    }
}