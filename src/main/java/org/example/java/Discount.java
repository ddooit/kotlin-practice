package org.example.java;
import java.time.LocalDateTime;

public class Discount {
    private final DiscountStrategy strategy;
    private final double rateOrFixed;
    private final Priority priority;
    private final LocalDateTime start;
    private final LocalDateTime end;

    private Discount(final DiscountStrategy strategy, final double rateOrFixed, final Priority priority, final LocalDateTime start, final LocalDateTime end) {
        this.strategy = strategy;
        this.rateOrFixed = rateOrFixed;
        this.priority = priority;
        this.start = start;
        this.end = end;
    }

    public static Discount of(final DiscountStrategy strategy, final double rateOrFixed, final LocalDateTime start, final LocalDateTime end) {
        return new Discount(strategy, rateOrFixed, strategy.priorityWithPeriod(), start, end);
    }

    public static Discount of(final DiscountStrategy strategy, final double rateOrFixed) {
        return new Discount(strategy, rateOrFixed, strategy.priority(), LocalDateTime.MIN, LocalDateTime.MAX);
    }

    public Priority getPriority(final LocalDateTime target) {
        if (valid(target)) {
            return priority;
        }
        return Priority.LAST;
    }

    public double getPrice(final double price, final LocalDateTime target) {
        if (!valid(target)) {
            return Double.MAX_VALUE;
        }

        return Math.max(strategy.calculate(price, rateOrFixed), 0D);
    }

    private boolean valid(final LocalDateTime target) {
        return start.isBefore(target) && end.isAfter(target);
    }

}
