package org.example

import org.example.coupon.Coupon
import org.example.coupon.CouponIssueService
import org.example.java.member.Member
import java.time.LocalDateTime

class DiscountCalculator(
    val couponIssueService: CouponIssueService
) {
    fun calculate(cartItems: List<CartItem>, member: Member): List<DiscountedItem> {

        val issueCouponsFor = couponIssueService.issueCouponsFor(member)



//        cartItems.map { cartItem ->
//            {
//                val discountedPrice = cartItem.product.discountedPrice(LocalDateTime.now())
//                val bestCoupon = getBestCoupon(issueCouponsFor)
//
//
//                bestCoupon
//
//            }
//        }
//        cartItems.


        return emptyList()
    }

}