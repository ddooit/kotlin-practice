import coupon.CouponType
import coupon.CouponType.FREE_DELIVERY
import coupon.CouponType.TEN_PERCENT
import coupon.Member
import coupon.MemberGrade
import org.assertj.core.api.Assertions.assertThat
import org.example.coupon.Coupon
import org.example.coupon.Coupon.Companion.createFreeDelivery
import org.example.coupon.CouponIssueService
import org.example.coupon.CouponStore
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.temporal.ChronoUnit
import java.util.*

class CouponTest {

    private val underTest = CouponIssueService(MockedStore())

    @Test
    @DisplayName("BRONZE 등급은 어떤 쿠폰도 발급되지 않아야 한다")
    fun should_not_issue_coupon_when_BRONZE() {
        // given
        val bronzeMember = Member.of(MemberGrade.BRONZE)

        // when
        val actual = underTest.issueCouponsFor(bronzeMember)

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    @DisplayName("SILVER 등급은 배송비 무료 쿠폰만 발급 가능해야 한다")
    fun should_issue_free_delivery_coupon_when_SILVER() {
        // given
        val silverMember = Member.of(MemberGrade.SILVER)

        // when
        val actual = underTest.issueCouponsFor(silverMember)

        // then
        assertThat(actual).hasSize(1)
        assertThat(actual[0].type).isEqualTo(FREE_DELIVERY)
        assertThat(actual[0].issuedAt.plus(7L, ChronoUnit.DAYS)).isEqualTo(actual[0].validUntil)
    }

    @Test
    @DisplayName("GOLD/PLATINUM 등급은 배송비 무료 + 10% 쿠폰 모두 발급 가능해야 한다")
    fun should_issue_free_delivery_and_ten_percent_coupon_when_GOLD_or_PLATINUM() {
        // given
        val goldMember = Member.of(MemberGrade.GOLD)
        val platinumMember = Member.of(MemberGrade.PLATINUM)

        // when
        val actualGold = underTest.issueCouponsFor(goldMember)
        val actualPlatinum = underTest.issueCouponsFor(platinumMember)

        // then
        assertThat(actualGold).hasSize(2)
        assertThat(actualPlatinum).hasSize(2)
        assertThat(actualGold.stream().map { it.type }).contains(FREE_DELIVERY, TEN_PERCENT)
        assertThat(actualPlatinum.stream().map { it.type }).contains(FREE_DELIVERY, TEN_PERCENT)
    }

    @Test
    @DisplayName("동일 쿠폰을 이미 발급받은 경우 중복 발급되지 않아야 한다")
    fun should_not_issue_when_same_type_coupon_exist() {
        // given
        val underTestWithSameTypeCouponExist = CouponIssueService(MockedStoreWhenExistIssuedCoupon())
        val silverMember = Member.of(MemberGrade.SILVER)

        // when
        val actual = underTestWithSameTypeCouponExist.issueCouponsFor(silverMember)

        // then
        assertThat(actual).isEmpty()
    }

    @Test
    @DisplayName("하루 최대 발급 수 제한을 초과하면 발급이 차단되어야 한다")
    fun should_not_issue_when_max_over_count_per_day() {
        // given
        val underTestWithMaxOverCouponCount = CouponIssueService(MockedStoreWhenMaxOverCoupon())
        val silverMember = Member.of(MemberGrade.SILVER)

        // when
        val actual = underTestWithMaxOverCouponCount.issueCouponsFor(silverMember)

        // then
        assertThat(actual).isEmpty()
    }

    class MockedStore : CouponStore {
        override fun findIssuedCouponsByMemberId(memberId: UUID): List<Coupon> = emptyList()

        override fun remainingCountMap(): Map<CouponType, Int> = mapOf(FREE_DELIVERY to 1_000, TEN_PERCENT to 500)

        override fun issue(coupons: List<Coupon>): List<Coupon> = coupons
    }

    class MockedStoreWhenExistIssuedCoupon : CouponStore {
        override fun findIssuedCouponsByMemberId(memberId: UUID): List<Coupon> = listOf(createFreeDelivery(memberId))

        override fun remainingCountMap(): Map<CouponType, Int> = mapOf(FREE_DELIVERY to 1_000, TEN_PERCENT to 500)

        override fun issue(coupons: List<Coupon>): List<Coupon> = coupons
    }

    class MockedStoreWhenMaxOverCoupon : CouponStore {
        override fun findIssuedCouponsByMemberId(memberId: UUID): List<Coupon> = listOf(createFreeDelivery(memberId))

        override fun remainingCountMap(): Map<CouponType, Int> = mapOf(FREE_DELIVERY to 0, TEN_PERCENT to 500)

        override fun issue(coupons: List<Coupon>): List<Coupon> = coupons
    }
}