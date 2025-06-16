package n7.mcdalang;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.models.antlr.Translate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGrammarToC {

    @Test
    void declarationTest() {
        List<Pair<String, String>> declarations = List.of(
                Pair.of("var entier x = 10", "int x = 10;"),
                Pair.of("var flottant y = 3.14", "float y = 3.14;"),
                Pair.of("const chaine nom = \"Alice\"", "char* nom = \"Alice\";"),
                Pair.of("var bool estValide = true", "bool estValide = true;"),
                Pair.of("var char lettre = 'A'", "char lettre = 'A';")
        );

        for (Pair<String, String> declaration : declarations) {
            assertEquals(declaration.second.strip(), superTest(declaration.first + "\n").strip());
        }
    }

    @Test
    void afficherTest() {
        List<Pair<String, String>> printStatements = List.of(
                Pair.of("afficher(\"message\")", "printf(\"%d\\n\", \"message\");"),
                Pair.of("var entier x = 2\nafficher(x)", "int x = 2;\nprintf(\"%d\\n\", x);"),
                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)", "int x = 2;\nprintf(\"%d\\n\", \"compteur: \" & x);")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second.strip(), superTest(print.first + "\n").strip());
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("si (x < 5) {\n    afficher(\"x est inferieur a 5\")\n}",
                        "if (x < 5) {\nprintf(\"%d\\n\", \"x est inferieur a 5\");\n}")
                //Pair.of("si (x > 5) {\n    afficher(\"x est superieur a 5\")\n} sinon {\n    afficher(\"x est inferieur ou egal a 5\")\n}",
                //        "if (x > 5) {\nprintf(\"%d\\n\", \"x est superieur a 5\");\n} else {\nprintf(\"%d\\n\", \"x est inferieur ou egal a 5\");\n}"),
                //Pair.of("si (x < 0) {\n    afficher(\"x est negatif\")\n} snsi (x == 0) {\n    afficher(\"x est nul\")\n} sinon {\n    afficher(\"x est positif\")\n}",
                //        "if (x < 0) {\nprintf(\"%d\\n\", \"x est negatif\");\n} else if (x == 0) {\nprintf(\"%d\\n\", \"x est nul\");\n} else {\nprintf(\"%d\\n\", \"x est positif\");\n}")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second.strip(), superTest(condition.first + "\n").strip());
        }
    }
/**
    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("tantque (x < 15) {\n    afficher(x)\n    x = x + 1\n}",
                        "while (x < 15) {\n    printf(\"%d\\n\", x);\n    x = x + 1;\n}"),
                Pair.of("faire {\n    afficher(\"Exécution\")\n    y = y - 1\n} tantque (y > 0)",
                        "do {\n    printf(\"%s\\n\", \"Exécution\");\n    y = y - 1;\n} while (y > 0);"),
                Pair.of("pour (x = 0; x < 5; x = x + 1) {\n    afficher(\"compteur: \" & x)\n}",
                        "for (x = 0; x < 5; x = x + 1) {\n    printf(\"%s%d\\n\", \"compteur: \", x);\n}")
        );

        for (Pair<String, String> loop : loops) {
            assertEquals(loop.second.strip(), superTest(loop.first + "\n").strip());
        }
    }
**/

    @Test
    void methodTest() {
        List<Pair<String, String>> methods = List.of(
                Pair.of("methode vide test() {}", "void test() {\n}"),
                Pair.of("methode entier test(entier a, entier b) {}", "int test(int a, int b) {\n}")
        );

        for (Pair<String, String> method : methods) {
            assertEquals(method.second.strip(), superTest(method.first + "\n").strip());
        }
    }

    private static String superTest(String input) {
        return Translate.translateToOther(input, Languages.C).replace("#include <stdio.h>\n", "").replaceAll("  ", "");
    }
}
