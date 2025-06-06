package n7.mcdalang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExampleTest {

    @Test
    void declarationTest() {
        String input =
            """
            var entier x = 10
            """;
        String expected =
            """
            int x = 10;
            """;
        assertEquals(expected, superTest(input));
        input =
            """
            var flottant y = 3.14
            """;
        expected =
            """
            float y = 3.14;
            """;
        assertEquals(expected, superTest(input));  
        input =
            """
            const chaine nom = "Alice"
            """;
        expected =
            """
            const char* nom = "Alice";
            """;
        assertEquals(expected, superTest(input));   
        input =
            """
            var bool estValide = true
            """;
        expected =
            """
            bool estValide = true;
            """;
        assertEquals(expected, superTest(input));  
        input =
            """
            var char lettre = 'A'
            """;
        expected =
            """
            char lettre = "A";
            """;
        assertEquals(expected, superTest(input));                       
    }

    @Test
    void afficherTest() {
        String input =
            """
            afficher("message")
            """;
        String expected =
            """
            printf("message");
            """;
        assertEquals(expected, superTest(input));  
        input =
            """
            var entier x = 2
            afficher(x)
            """;
        expected =
            """
            int x = 2;
            printf(x);
            """;
        assertEquals(expected, superTest(input));   
        input =
            """
            var entier x = 2
            afficher("compteur: " & x)
            """;
        expected =
            """
            int x = 2;
            printf("compteur: %d\n", x);
            """;
        assertEquals(expected, superTest(input));                                             
    }    

    @Test
    void conditionTest() {
        String input =
            """
            si (x < 5) {
                afficher("x est inférieur à 5")
            }
            """;
        String expected =
            """
            if (x < 5) {
                printf("x est inférieur à 5");
            }
            """;
        assertEquals(expected, superTest(input));
        input =
            """
            si (x > 5) {
                afficher("x est supérieur à 5")
            } sinon {
                afficher("x est inférieur ou égal à 5")
            }
            """;
        expected =
            """
            if (x > 5) {
                printf("x est supérieur à 5");
            } else {
                printf("x est inférieur ou égal à 5");
            }
            """;
        assertEquals(expected, superTest(input));   
        input =
            """
            si (x < 0) {
                afficher("x est négatif")
            } snsi (x == 0) {
                afficher("x est nul")
            } sinon {
                afficher("x est positif")
            }
            """;
        expected =
            """
            if (x < 0) {
                printf("x est négatif");
            } else if (x == 0) {
                printf("x est nul");
            } else {
                printf("x est positif"); 
            }
            """;
        assertEquals(expected, superTest(input));                             
    }

    @Test
    void boucleTest() {
        String input =
            """
            tantque (x < 15) {
                afficher(x)
                x = x + 1
            }
            """;
        String expected =
            """
            while (x < 15) {
                printf(x);
                x = x + 1;
            }
            """;
        assertEquals(expected, superTest(input));
        input =
            """
            faire {
                afficher("Exécution au moins une fois")
                y = y - 1
            } tantque (y > 0)
            """;
        expected =
            """
            do {
                printf("Exécution au moins une fois");
                y = y - 1;
            } while (y > 0);
            """;
        assertEquals(expected, superTest(input));    
        input =
            """
            pour (x = 0; x < 5; x++) {
                afficher("compteur: " & x)
            }
            """;
        expected =
            """
            for (int x = 0; x < 5; x++) {
                printf("compteur: %d\n", x);
            }
            """;
        assertEquals(expected, superTest(input));                                    
    }


    private static String superTest(String input) {
        return input + "works\n";
    }
}
