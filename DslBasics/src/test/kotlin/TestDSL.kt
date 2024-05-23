import org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class SimpleAssertionsExample {
    @Test
    fun a_few_simple_assertions() {
        assertThat("The Lord of the Rings").isNotNull()
            .startsWith("The")
            .contains("Lord")
            .endsWith("Rings");
    }
}
