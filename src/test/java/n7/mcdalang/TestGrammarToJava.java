package n7.mcdalang;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.models.antlr.Translate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGrammarToJava {

    @Test
    void declarationTest() {
        List<Pair<String, String>> declarations = List.of(
                Pair.of("var entier x = 10", "int x = 10;"),
                Pair.of("var flottant y = 3.14", "float y = 3.14;"),
                Pair.of("const chaine nom = \"Alice\"", "String nom = \"Alice\";"),
                Pair.of("var bool estValide = true", "boolean estValide = true;"),
                Pair.of("var char lettre = 'A'", "char lettre = 'A';")
        );

        for (Pair<String, String> declaration : declarations) {
            assertEquals(declaration.second.strip(), superTest(declaration.first + "\n").strip());
        }
    }

    @Test
    void afficherTest() {
        List<Pair<String, String>> printStatements = List.of(
                Pair.of("afficher(\"message\")", "System.out.println(\"message\");"),
                Pair.of("var entier x = 2\nafficher(x)", "int x = 2;\nSystem.out.println(x);"),
                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)", "int x = 2;\nSystem.out.println(\"compteur: \" + x);")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second.strip(), superTest(print.first + "\n").strip());
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("si (x < 5) {\n    afficher(\"x est inferieur a 5\")\n}",
                        "if (x < 5) {\n    System.out.println(\"x est inferieur a 5\");\n}"),
                Pair.of("si (x > 5) {\n    afficher(\"x est superieur a 5\")\n} sinon {\n    afficher(\"x est inferieur ou egal a 5\")\n}",
                        "if (x > 5) {\n    System.out.println(\"x est superieur a 5\");\n} else {\n    System.out.println(\"x est inferieur ou egal a 5\");\n}"),
                Pair.of("si (x < 0) {\n    afficher(\"x est negatif\")\n} snsi (x == 0) {\n    afficher(\"x est nul\")\n} sinon {\n    afficher(\"x est positif\")\n}",
                        "if (x < 0) {\n    System.out.println(\"x est negatif\");\n} else if (x == 0) {\n    System.out.println(\"x est nul\");\n} else {\n    System.out.println(\"x est positif\");\n}")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second.strip(), superTest(condition.first + "\n").strip());
        }
    }

    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("tantque (x < 15) {\n    afficher(x)\n    x = x + 1\n}",
                        "while (x < 15) {\n    System.out.println(x);\n    x = x + 1;\n}"),
                Pair.of("faire {\n    afficher(\"Exécution\")\n    y = y - 1\n} tantque (y > 0)",
                        "do {\n    System.out.println(\"Exécution\");\n    y = y - 1;\n} while (y > 0);"),
                Pair.of("pour (x = 0; x < 5; x = x + 1) {\n    afficher(\"compteur: \" & x)\n}",
                        "for (x = 0; x < 5; x = x + 1) {\n    System.out.println(\"compteur: \" + x);\n}")
        );

        for (Pair<String, String> loop : loops) {
            assertEquals(loop.second.strip(), superTest(loop.first + "\n").strip());
        }
    }

    @Test
    void expressionsTest() {
        List<Pair<String, String>> expressions = List.of(
                Pair.of("+", "+"),
                Pair.of("-", "-"),
                Pair.of("*", "*"),
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
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }

        List<Pair<String, String>> increments = List.of(
                Pair.of("++", "++"),
                Pair.of("--", "--")
        );
        for(Pair<String, String> increment : increments) {
            String input = "a" + increment.first;
            String expected = "a" + increment.second + ";";
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }
    }

    @Test
    void methodTest() {
        List<Pair<String, String>> methods = List.of(
                Pair.of("methode vide test() {}", "public static void test() {\n}\n"),
                Pair.of("methode entier test(entier a, entier b) {}", "public static int test(int a, int b) {\n}\n")
        );

        for (Pair<String, String> method : methods) {
            assertEquals(method.second.strip(), superTest(method.first + "\n").strip());
        }
    }

    private static String superTest(String input) {
        return Translate.translateToOther(input, Languages.JAVA).strip();
    }
}
