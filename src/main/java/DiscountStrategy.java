public enum DiscountStrategy {
    FIXED {
        @Override
        public double calculate(final double price, final double rateOrFixed) {
            return price - rateOrFixed;
        }

        @Override
        public EnumPriority priority() {
            return EnumPriority.FOURTH;
        }

        @Override
        public EnumPriority priorityWithPeriod() {
            return EnumPriority.SECOND;
        }
    },
    RATE {
        @Override
        public double calculate(final double price, final double rateOrFixed) {
            return price * (1 - rateOrFixed);
        }

        @Override
        public EnumPriority priority() {
            return EnumPriority.THIRD;
        }

        @Override
        public EnumPriority priorityWithPeriod() {
            return EnumPriority.FIRST;
        }
    };

    public abstract double calculate(double price, double rateOrFixed);

    public abstract EnumPriority priority();

    public abstract EnumPriority priorityWithPeriod();
}
