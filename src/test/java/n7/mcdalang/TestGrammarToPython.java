package n7.mcdalang;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.models.antlr.Translate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGrammarToPython {

    @Test
    void declarationTest() {
        List<Pair<String, String>> declarations = List.of(
                Pair.of("var entier x = 10", "x = 10"),
                Pair.of("var flottant y = 3.14", "y = 3.14"),
                Pair.of("const chaine nom = \"Alice\"", "nom = \"Alice\""),
                Pair.of("var bool estValide = true", "estValide = True"),
                Pair.of("var char lettre = 'A'", "lettre = \'A\'")
        );

        for (Pair<String, String> declaration : declarations) {
            assertEquals(declaration.second + "\n", superTest(declaration.first + "\n"));
        }
    }

    @Test
    void afficherTest() {
        List<Pair<String, String>> printStatements = List.of(
                Pair.of("afficher(\"message\")", "print(\"message\")"),

                Pair.of("var entier x = 2\nafficher(x)", "x = 2\nprint(x)"),

                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)",
                        "x = 2\nprint(\"compteur: \" + x)")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second + "\n", superTest(print.first + "\n"));
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("si (x < 5) {\n    afficher(\"x est inférieur à 5\")\n}",
                        "if x < 5:\n    print(\"x est inférieur à 5\")"),

                Pair.of("si (x > 5) {\n    afficher(\"x est supérieur à 5\")\n} sinon {\n    afficher(\"x est inférieur ou égal à 5\")\n}",
                        "if x > 5:\n    print(\"x est supérieur à 5\")\nelse:\n    print(\"x est inférieur ou égal à 5\")"),

                Pair.of("si (x < 0) {\n    afficher(\"x est négatif\")\n} snsi (x == 0) {\n    afficher(\"x est nul\")\n} sinon {\n    afficher(\"x est positif\")\n}",
                        "if x < 0:\n    print(\"x est négatif\")\nelif x == 0:\n    print(\"x est nul\")\nelse:\n    print(\"x est positif\")")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second + "\n", superTest(condition.first + "\n"));
        }
    }

    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("tantque (x < 15) {\n    afficher(x)\n    x = x + 1\n}",
                        "while x < 15:\n    print(x)\n    x = x + 1"),
                Pair.of("faire {\n    afficher(\"Exécution au moins une fois\")\n    y = y - 1\n} tantque (y > 0)",
                        "while True:\n    print(\"Exécution au moins une fois\")\n    y = y - 1\n    if not (y > 0): break"),
                Pair.of("pour (x = 0; x < 5; x++) {\n    afficher(\"compteur: \" & x)\n}",
                        "for x in range(0, 5):\n    print(\"compteur: \" + x)")
        );

        for (Pair<String, String> loop : loops) {
            assertEquals(loop.second + "\n", superTest(loop.first + "\n"));
        }
    }

    @Test
    void expressionsTest() {
        // Expressions between two variables (assignation included)
        List<Pair<String, String>> expressions = List.of(
                Pair.of("=", "="),
                Pair.of("+", "+"),
                Pair.of("-", "-"),
                Pair.of("*", "*"),
                Pair.of("^", "**"),
                Pair.of("/", "/"),
                Pair.of("//", "//"),
                Pair.of("%", "%"),
                Pair.of(">", ">"),
                Pair.of("<", "<"),
                Pair.of(">=", ">="),
                Pair.of("<=", "<="),
                Pair.of("==", "=="),
                Pair.of("!=", "!="),
                Pair.of("&", "+")
        );
        for(Pair<String, String> expression : expressions) {
            String input = "a " + expression.first + " b";
            String expected = "a " + expression.second + " b";
            assertEquals(expected, superTest(input));
        }

        // Expressions with a constant
        List<Pair<String, String>> increments = List.of(
                Pair.of("++", " = a + 1"),
                Pair.of("--", " = a - 1")
        );
        for(Pair<String, String> increment : increments) {
            String input = "a" + increment.first;
            String expected = "a" + increment.second;
            assertEquals(expected + "\n", superTest(input + "\n"));
            String reverseInput = increment.first + "a";
            String reverseExpected = "a" + increment.second;
            assertEquals(reverseExpected + "\n", superTest(reverseInput + "\n"));
        }
    }

    @Test
    void methodsTest() {
        List<Pair<String, String>> methods = List.of(
                Pair.of("methode vide test() {}", "def test():\n"),
                Pair.of("methode entier test(entier a, entier b) {}", "def test(a, b):\n"),
                Pair.of("a.method()", "a.method()"),
                Pair.of("method(a)", "method(a)"),
                Pair.of("method()", "method()")
        );
        for(Pair<String, String> method : methods) {
            String input = method.first;
            String expected = method.second;
            assertEquals(expected, superTest(input));
        }
    }

    private static String superTest(String input) {
        try {
            return Translate.translateToOther(input, Languages.PYTHON);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}