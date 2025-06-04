import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductPriceCalculatorTest {

    @Test
    @DisplayName("정가만 설정된 상품은 원가 그대로 반환해야 한다")
    void should_return_original_price_given_product_has_only_original_price() {
        // given
        final var product = new Product(
                RandomTestUtils.randomLong(),
                RandomTestUtils.randomAlphaString(),
                RandomTestUtils.randomDouble(),
                List.of());

        // when
        final var actual = product.discountedPrice(RandomTestUtils.randomLocalDateTime());

        // then
        assertEquals(product.price(), actual);
    }

    @Test
    @DisplayName("정액 할인 정책이 적용되면 원가에서 할인 금액만큼 차감되어야 한다")
    void should_discount_when_apply_fixed_discount_policy() {
        // given
        final var product = new Product(
                RandomTestUtils.randomLong(),
                RandomTestUtils.randomAlphaString(),
                3_000D,
                List.of(Discount.of(DiscountStrategy.FIXED, 1_000D)));

        // when
        final var actual = product.discountedPrice(RandomTestUtils.randomLocalDateTime());

        // then
        assertEquals(2_000D, actual);
    }

    @Test
    @DisplayName("정률 할인 정책이 적용되면 원가 * (1 - rate) 값이 반환되어야 한다")
    void should_discount_when_apply_rate_discount_policy() {
        // given
        final var product = new Product(
                RandomTestUtils.randomLong(),
                RandomTestUtils.randomAlphaString(),
                3_000D,
                List.of(Discount.of(DiscountStrategy.RATE, 0.1D)));

        // when
        final var actual = product.discountedPrice(RandomTestUtils.randomLocalDateTime());

        // then
        assertEquals(2_700D, actual);
    }

    @Test
    @DisplayName("기간 한정 할인 정책이 적용 중일 경우, 다른 정책보다 우선되어야 한다")
    void should_super_priority_when_apply_period_discount() {
        // given
        final var product = new Product(
                RandomTestUtils.randomLong(),
                RandomTestUtils.randomAlphaString(),
                3_000D,
                List.of(Discount.of(DiscountStrategy.RATE, 0.1D, LocalDateTime.MIN, LocalDateTime.MAX),
                        Discount.of(DiscountStrategy.FIXED, 1_000D)));

        // when
        final var actual = product.discountedPrice(RandomTestUtils.randomLocalDateTime());

        // then
        assertEquals(2_700D, actual);
    }

    @Test
    @DisplayName("기간 한정 할인 정책이 유효 기간이 지났을 경우 무시되어야 한다")
    void should_skip_when_invalid_period() {
        // given
        final var start = LocalDateTime.of(2025, 1, 1, 0, 0);
        final var end = LocalDateTime.of(2026, 1, 1, 0, 0);
        final var product = new Product(
                RandomTestUtils.randomLong(),
                RandomTestUtils.randomAlphaString(),
                3_000D,
                List.of(Discount.of(DiscountStrategy.RATE, 0.1D, start, end)));

        // when
        final var actual = product.discountedPrice(LocalDateTime.of(2027, 1, 1, 0, 0));

        // then
        assertEquals(3_000D, actual);
    }

    @Test
    @DisplayName("할인 적용 후 가격이 0보다 작을 경우 0으로 제한해야 한다")
    void should_discounted_price_non_negative() {
        // given
        final var product = new Product(
                RandomTestUtils.randomLong(),
                RandomTestUtils.randomAlphaString(),
                3_000D,
                List.of(Discount.of(DiscountStrategy.FIXED, 4_000D)));

        // when
        final var actual = product.discountedPrice(RandomTestUtils.randomLocalDateTime());

        // then
        assertEquals(0D, actual);
    }

    @Test
    @DisplayName("여러 정책이 동시에 존재할 경우 우선순위대로 하나만 적용되어야 한다")
    void should_apply_only_one_strategy() {
        // given
        final var product = new Product(
                RandomTestUtils.randomLong(),
                RandomTestUtils.randomAlphaString(),
                3_000D,
                List.of(
                        Discount.of(DiscountStrategy.RATE, 0.1D, LocalDateTime.MIN, LocalDateTime.MAX),
                        Discount.of(DiscountStrategy.FIXED, 3_000D, LocalDateTime.MIN, LocalDateTime.MAX),
                        Discount.of(DiscountStrategy.RATE, 0.2D),
                        Discount.of(DiscountStrategy.FIXED, 4_000D)
                ));

        // when
        final var actual = product.discountedPrice(RandomTestUtils.randomLocalDateTime());

        // then
        assertEquals(2_700D, actual);
    }
}