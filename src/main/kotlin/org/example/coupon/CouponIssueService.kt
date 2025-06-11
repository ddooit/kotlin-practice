package org.example.coupon

import org.example.java.member.Member

interface CouponIssueService {
    fun issueCouponsFor(member: Member): List<Coupon>
}