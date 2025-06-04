import java.util.Comparator;

public enum EnumPriority {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    LAST(99);

    private final int value;

    EnumPriority(final int value) {
        this.value = value;
    }

    public static Comparator<EnumPriority> getComparator() {
        return (enum1, enum2) -> enum2.value - enum1.value;
    }

}
