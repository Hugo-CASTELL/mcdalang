package n7.mcdalang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExampleTest {

    @Test
    void exampleTest() {
        String input =
            """
            test
            """;
        String expected =
            """
            test
            works
            """;
        assertEquals(expected, superTest(input));
    }

    private static String superTest(String input) {
        return input + "works\n";
    }
}
