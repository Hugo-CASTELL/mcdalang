package n7.mcdalang;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.models.antlr.Translate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGrammarToC {

    @Test
    void declarationTest() {
        List<Pair<String, String>> declarations = List.of(
                Pair.of("var entier x = 10", "int x = 10;"),
                Pair.of("var flottant y = 3.14", "float y = 3.14;"),
                Pair.of("const chaine nom = \"Alice\"", "const char* nom = \"Alice\";"),
                Pair.of("var bool estValide = true", "bool estValide = true;"),
                Pair.of("var char lettre = 'A'", "char lettre = \"A\";")
        );

        for (Pair<String, String> declaration : declarations) {
            assertEquals(declaration.second, superTest(declaration.first));
        }
    }

    @Test
    void afficherTest() {
        List<Pair<String, String>> printStatements = List.of(
                Pair.of("afficher(\"message\")", "printf(\"message\");"),

                Pair.of("var entier x = 2\nafficher(x)", "int x = 2;\nprintf(x);"),

                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)",
                        "int x = 2;\nprintf(\"compteur: %d\\n\", x);")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second, superTest(print.first));
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("si (x < 5) {\n    afficher(\"x est inférieur à 5\")\n}",
                        "if (x < 5) {\n    printf(\"x est inférieur à 5\");\n}"),

                Pair.of("si (x > 5) {\n    afficher(\"x est supérieur à 5\")\n} sinon {\n    afficher(\"x est inférieur ou égal à 5\")\n}",
                        "if (x > 5) {\n    printf(\"x est supérieur à 5\");\n} else {\n    printf(\"x est inférieur ou égal à 5\");\n}"),

                Pair.of("si (x < 0) {\n    afficher(\"x est négatif\")\n} snsi (x == 0) {\n    afficher(\"x est nul\")\n} sinon {\n    afficher(\"x est positif\")\n}",
                        "if (x < 0) {\n    printf(\"x est négatif\");\n} else if (x == 0) {\n    printf(\"x est nul\");\n} else {\n    printf(\"x est positif\"); \n}")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second, superTest(condition.first));
        }
    }

    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("tantque (x < 15) {\n    afficher(x)\n    x = x + 1\n}",
                        "while (x < 15) {\n    printf(x);\n    x = x + 1;\n}"),
                Pair.of("faire {\n    afficher(\"Exécution au moins une fois\")\n    y = y - 1\n} tantque (y > 0)",
                        "do {\n    printf(\"Exécution au moins une fois\");\n    y = y - 1;\n} while (y > 0);"),
                Pair.of("pour (x = 0; x < 5; x++) {\n    afficher(\"compteur: \" & x)\n}",
                        "for (int x = 0; x < 5; x++) {\n    printf(\"compteur: %d\\n\", x);\n}")
        );

        for (Pair<String, String> loop : loops) {
            assertEquals(loop.second, superTest(loop.first));
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
                Pair.of("//", "/"),
                Pair.of("%", "%"),
                Pair.of(">", ">"),
                Pair.of("<", "<"),
                Pair.of(">=", ">="),
                Pair.of("<=", "<="),
                Pair.of("==", "=="),
                Pair.of("!=", "!="),
                Pair.of("&", "&")
        );
        for(Pair<String, String> expression : expressions) {
            String input = "a " + expression.first + " b";
            String expected = "a " + expression.second + " b";
            assertEquals(expected, superTest(input));
        }

        // Expressions with a constant
        List<Pair<String, String>> increments = List.of(
                Pair.of("++", "++"),
                Pair.of("--", "--")
        );
        for(Pair<String, String> increment : increments) {
            String input = "a" + increment.first;
            String expected = "a" + increment.second;
            assertEquals(expected, superTest(input));
            String reverseInput = increment.first + "a";
            String reverseExpected = increment.second + "a";
            assertEquals(reverseExpected, superTest(reverseInput));
        }
    }

    @Test
    void methodsTest() {
        List<Pair<String, String>> methods = List.of(
                Pair.of("methode vide test() {}", "void test() {\n}"),
                Pair.of("methode entier test(entier a, entier b) {}", "int test(int a, int b) {\n}"),
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
        return Translate.translateToOther(input, Languages.C).replace("#include <stdio.h>\n\n", "");
    }
}
