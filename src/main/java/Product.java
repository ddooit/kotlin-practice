import java.time.LocalDateTime;
import java.util.List;

public record Product(
        long id,
        String name,
        double price,
        List<Discount> discountList
) {
    public double discountedPrice(final LocalDateTime target) {
        final var appliedDiscounts = appliedDiscounts(target);

        if (!appliedDiscounts.isEmpty()) {
            return getMinPrice(target, appliedDiscounts);
        }

        return getMinPrice(target, discountList);

    }

    private List<Discount> appliedDiscounts(final LocalDateTime target) {
        if (discountList.isEmpty()) {
            return List.of();
        }
        final var maxPriority = discountList.stream()
                .map(discount -> discount.getPriority(target))
                .max(Priority.getComparator())
                .orElseThrow();

        return discountList.stream()
                .filter(discount -> discount.getPriority(target) == maxPriority)
                .toList();
    }

    private double getMinPrice(final LocalDateTime target, final List<Discount> discountList) {
        var minPrice = price;
        for (final var discount : discountList) {
            final var discountedPrice = discount.getPrice(price, target);

            if (discountedPrice < minPrice) {
                minPrice = discountedPrice;
            }
        }

        return minPrice;
    }
}
