import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PriorityTest {

    @Test
    void sortTest() {
        // given
        final var enumPriorities = List.of(Priority.FIRST, Priority.SECOND, Priority.THIRD);

        // when
        final var list = enumPriorities.stream()
                .max(Priority.getComparator());

        // then
        assertThat(list.get()).isEqualTo(Priority.FIRST);
    }
}