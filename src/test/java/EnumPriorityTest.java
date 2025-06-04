import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EnumPriorityTest {

    @Test
    void sortTest() {
        // given
        final var enumPriorities = List.of(EnumPriority.FIRST, EnumPriority.SECOND, EnumPriority.THIRD);

        // when
        final var list = enumPriorities.stream()
                .max(EnumPriority.getComparator());

        // then
        assertThat(list.get()).isEqualTo(EnumPriority.FIRST);
    }
}