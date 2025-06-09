package org.example.coupon

import coupon.Member
import coupon.MemberGrade
import org.example.coupon.Coupon.Companion.createFreeDelivery
import org.example.coupon.Coupon.Companion.createTenPercent

/**
 * object 키워드를 사용해서 인스턴스 생성 없이 직접 함수 호출이 가능
 * 싱글톤으로 정적인 역할만 하도록 만들기 위함
 */
object CouponFactory {

    fun byMember(member: Member): List<Coupon> = when (member.grade()) {
        MemberGrade.BRONZE ->
            emptyList()

        MemberGrade.SILVER ->
            listOf(createFreeDelivery(member.id()))

        MemberGrade.GOLD, MemberGrade.PLATINUM ->
            listOf(createFreeDelivery(member.id()), createTenPercent(member.id()))
    }
}