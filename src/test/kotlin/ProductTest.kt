import org.example.DiscountCondition
import org.example.Product
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Instant

class ProductTest {

    @Test
    @DisplayName("정가만 설정된 상품은 원가 그대로 반환해야 한다")
    fun should_return_original_price_given_product_has_only_original_price() {
        // given
        val product = Product(
            RandomTestUtils.randomLong(),
            RandomTestUtils.randomAlphaString(),
            RandomTestUtils.randomDouble(),
            RandomTestUtils.randomDouble(),
            listOf()
        )

        // when
        val actual = product.discountedPrice(Instant.now())

        // then
        Assertions.assertEquals(product.price, actual)
    }

    @Test
    @DisplayName("정액 할인 정책이 적용되면 원가에서 할인 금액만큼 차감되어야 한다")
    fun should_discount_when_apply_fixed_discount_policy() {
        // given
        val product = Product(
            RandomTestUtils.randomLong(),
            RandomTestUtils.randomAlphaString(),
            3000.0,
            0.0,
            listOf(DiscountCondition.Fixed(1_000.0))
        )

        // when
        val actual = product.discountedPrice(Instant.now())

        // then
        Assertions.assertEquals(2000.0, actual)
    }

    @Test
    @DisplayName("정률 할인 정책이 적용되면 원가 * (1 - rate) 값이 반환되어야 한다")
    fun should_discount_when_apply_rate_discount_policy() {
        // given
        val product = Product(
            RandomTestUtils.randomLong(),
            RandomTestUtils.randomAlphaString(),
            3000.0,
            0.0,
            listOf(DiscountCondition.Rate(0.1))
        )

        // when
        val actual = product.discountedPrice(Instant.now())

        // then
        Assertions.assertEquals(2700.0, actual)
    }

    @Test
    @DisplayName("기간 한정 할인 정책이 적용 중일 경우, 다른 정책보다 우선되어야 한다")
    fun should_super_priority_when_apply_period_discount() {
        // given
        val product = Product(
            RandomTestUtils.randomLong(),
            RandomTestUtils.randomAlphaString(),
            3000.0,
            0.0,
            listOf(
                DiscountCondition.Rate(0.1),
                DiscountCondition.Fixed(1_000.0)
            )
        )

        // when
        val actual = product.discountedPrice(Instant.now())

        // then
        Assertions.assertEquals(2700.0, actual)
    }

    @Test
    @DisplayName("기간 한정 할인 정책이 유효 기간이 지났을 경우 무시되어야 한다")
    fun should_skip_when_invalid_period() {
        // given
        val start = Instant.parse("2025-01-01T00:00:00.00Z")
        val end = Instant.parse("2026-01-01T00:00:00.00Z")
        val product = Product(
            RandomTestUtils.randomLong(),
            RandomTestUtils.randomAlphaString(),
            3000.0,
            0.0,
            listOf(DiscountCondition.Rate(rate = 0.1, period = DiscountCondition.Period(start, end)))
        )

        // when
        val actual = product.discountedPrice(Instant.parse("2027-01-01T00:00:00.00Z"))

        // then
        Assertions.assertEquals(3000.0, actual)
    }

    @Test
    @DisplayName("할인 적용 후 가격이 0보다 작을 경우 0으로 제한해야 한다")
    fun should_discounted_price_non_negative() {
        // given
        val product = Product(
            RandomTestUtils.randomLong(),
            RandomTestUtils.randomAlphaString(),
            3000.0,
            0.0,
            listOf(DiscountCondition.Fixed(4_000.0))
        )

        // when
        val actual = product.discountedPrice(Instant.now())

        // then
        Assertions.assertEquals(0.0, actual)
    }

    @Test
    @DisplayName("여러 정책이 동시에 존재할 경우 우선순위대로 하나만 적용되어야 한다")
    fun should_apply_only_one_strategy() {
        // given
        val product = Product(
            RandomTestUtils.randomLong(),
            RandomTestUtils.randomAlphaString(),
            3000.0,
            0.0,
            listOf(
                DiscountCondition.Rate(0.1),
                DiscountCondition.Fixed(amount = 3_000.0),
                DiscountCondition.Rate(0.2),
                DiscountCondition.Fixed(amount = 4_000.0)
            )
        )

        // when
        val actual = product.discountedPrice(Instant.now())

        // then
        Assertions.assertEquals(2700.0, actual)
    }

}