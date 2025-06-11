import RandomTestUtils.*
import org.example.CartItem
import org.example.DiscountCalculator
import org.example.Product
import org.example.coupon.Coupon
import org.example.coupon.CouponIssueService
import org.example.java.member.Member
import org.example.java.member.MemberGrade
import org.junit.jupiter.api.Test

class DiscountCalculatorServiceTest {

    private companion object{
        fun underTest(coupons: List<Coupon>) = DiscountCalculator(WithCoupon(coupons))

        class WithCoupon(val givenCoupons: List<Coupon>): CouponIssueService{
            override fun issueCouponsFor(member: Member): List<Coupon> = givenCoupons
        }

        fun randomMember(): Member = Member.of(MemberGrade.entries.random())

        val product1 = Product(randomLong(), randomAlphaString(), randomDouble(), 0.0,listOf())
        val product2 = Product(randomLong(), randomAlphaString(), randomDouble(), 0.0, listOf())
    }

    @Test
    fun name() {
        // given
        val cartItems = listOf(
            CartItem(product = product1, quantity = randomInt()),
            CartItem(product = product2, quantity = randomInt())
        )
        val member = randomMember()

        // when
        val actual = underTest(coupons = emptyList())
            .calculate(cartItems, member)

        // then

    }





}