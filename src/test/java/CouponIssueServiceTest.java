import coupon.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static coupon.CouponType.FREE_DELIVERY;
import static coupon.CouponType.TEN_PERCENT;
import static coupon.MemberGrade.*;
import static org.assertj.core.api.Assertions.assertThat;

class CouponIssueServiceTest {

    private final CouponIssueService underTest = new CouponIssueService(new MockedStore());

    @Test
    @DisplayName("BRONZE 등급은 어떤 쿠폰도 발급되지 않아야 한다")
    void should_not_issue_coupon_when_BRONZE() {
        // given
        final var bronzeMember = Member.of(BRONZE);

        // when
        final var actual = underTest.getCoupons(bronzeMember);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("SILVER 등급은 배송비 무료 쿠폰만 발급 가능해야 한다")
    void should_issue_free_delivery_coupon_when_SILVER() {
        // given
        final var silverMember = Member.of(SILVER);

        // when
        final var actual = underTest.getCoupons(silverMember);

        // then
        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().type()).isEqualTo(FREE_DELIVERY);
        assertThat(actual.getFirst().issuedAt().plus(7L, ChronoUnit.DAYS)).isEqualTo(actual.getFirst().validUntil());
    }

    @Test
    @DisplayName("GOLD/PLATINUM 등급은 배송비 무료 + 10% 쿠폰 모두 발급 가능해야 한다")
    void should_issue_free_delivery_and_ten_percent_coupon_when_GOLD_or_PLATINUM() {
        // given
        final var goldMember = Member.of(GOLD);
        final var platinumMember = Member.of(PLATINUM);

        // when
        final var actualGold = underTest.getCoupons(goldMember);
        final var actualPlatinum = underTest.getCoupons(platinumMember);

        // then
        assertThat(actualGold).hasSize(2);
        assertThat(actualPlatinum).hasSize(2);
        assertThat(actualGold.stream().map(Coupon::type)).contains(FREE_DELIVERY, TEN_PERCENT);
        assertThat(actualPlatinum.stream().map(Coupon::type)).contains(FREE_DELIVERY, TEN_PERCENT);
    }

    @Test
    @DisplayName("동일 쿠폰을 이미 발급받은 경우 중복 발급되지 않아야 한다")
    void should_not_issue_when_same_type_coupon_exist() {
        // given
        final CouponIssueService underTestWithSameTypeCouponExist = new CouponIssueService(new MockedStoreWhenExistIssuedCoupon());
        final var silverMember = Member.of(SILVER);

        // when
        final var actual = underTestWithSameTypeCouponExist.getCoupons(silverMember);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("하루 최대 발급 수 제한을 초과하면 발급이 차단되어야 한다")
    void should_not_issue_when_max_over_count_per_day() {
        // given
        final CouponIssueService underTestWithMaxOverCouponCount = new CouponIssueService(new MockedStoreWhenMaxOverCoupon());
        final var silverMember = Member.of(SILVER);

        // when
        final var actual = underTestWithMaxOverCouponCount.getCoupons(silverMember);

        // then
        assertThat(actual).isEmpty();
    }

    private static class MockedStore implements CouponStore {

        @Override
        public List<Coupon> findIssuedCouponsByMemberId(final UUID memberId) {
            return List.of();
        }

        @Override
        public Map<CouponType, Integer> remainingCountMap() {
            return Map.of(FREE_DELIVERY, 1_000, TEN_PERCENT, 500);
        }

        @Override
        public List<Coupon> issue(final List<Coupon> coupons) {
            return coupons;
        }
    }

    private static class MockedStoreWhenExistIssuedCoupon implements CouponStore {

        @Override
        public List<Coupon> findIssuedCouponsByMemberId(final UUID memberId) {
            return List.of(Coupon.of(UUID.randomUUID(), FREE_DELIVERY, RandomTestUtils.randomAlphaString(), RandomTestUtils.randomAlphaString()));
        }

        @Override
        public Map<CouponType, Integer> remainingCountMap() {
            return Map.of(FREE_DELIVERY, 1_000, TEN_PERCENT, 500);
        }

        @Override
        public List<Coupon> issue(final List<Coupon> coupons) {
            return coupons;
        }
    }

    private static class MockedStoreWhenMaxOverCoupon implements CouponStore {

        @Override
        public List<Coupon> findIssuedCouponsByMemberId(final UUID memberId) {
            return List.of(Coupon.of(UUID.randomUUID(), FREE_DELIVERY, RandomTestUtils.randomAlphaString(), RandomTestUtils.randomAlphaString()));
        }

        @Override
        public Map<CouponType, Integer> remainingCountMap() {
            return Map.of(FREE_DELIVERY, 0, TEN_PERCENT, 500);
        }

        @Override
        public List<Coupon> issue(final List<Coupon> coupons) {
            return coupons;
        }
    }
}