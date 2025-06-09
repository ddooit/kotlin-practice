import java.util.Comparator;

public enum Priority {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    LAST(99);

    private final int value;

    Priority(final int value) {
        this.value = value;
    }

    public static Comparator<Priority> getComparator() {
        return (enum1, enum2) -> enum2.value - enum1.value;
    }

}
