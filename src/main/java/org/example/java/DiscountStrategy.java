package org.example.java;

public enum DiscountStrategy {
    FIXED {
        @Override
        public double calculate(final double price, final double rateOrFixed) {
            return price - rateOrFixed;
        }

        @Override
        public Priority priority() {
            return Priority.FOURTH;
        }

        @Override
        public Priority priorityWithPeriod() {
            return Priority.SECOND;
        }
    },
    RATE {
        @Override
        public double calculate(final double price, final double rateOrFixed) {
            return price * (1 - rateOrFixed);
        }

        @Override
        public Priority priority() {
            return Priority.THIRD;
        }

        @Override
        public Priority priorityWithPeriod() {
            return Priority.FIRST;
        }
    };

    public abstract double calculate(double price, double rateOrFixed);

    public abstract Priority priority();

    public abstract Priority priorityWithPeriod();
}
