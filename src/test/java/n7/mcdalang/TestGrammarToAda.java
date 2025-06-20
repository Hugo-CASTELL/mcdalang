package n7.mcdalang;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.models.antlr.Translate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGrammarToAda {

    @Test
    void declarationTest() {
        List<Pair<String, String>> declarations = List.of(
                Pair.of("var entier x = 10", "x : Integer := 10;"),
                Pair.of("var flottant y = 3.14", "y : Float := 3.14;"),
                Pair.of("const chaine nom = \"Alice\"", "nom : constant String := \"Alice\";"),
                Pair.of("var bool estValide = true", "estValide : Boolean := true;"),
                Pair.of("var char lettre = 'A'", "lettre : Character := 'A';")
        );

        for (Pair<String, String> declaration : declarations) {
            assertEquals(declaration.second.strip(), superTest(declaration.first + "\n").strip());
        }
    }

    @Test
    void afficherTest() {
        List<Pair<String, String>> printStatements = List.of(
                Pair.of("afficher(\"message\")", "Put_Line(\"message\");"),

                Pair.of("var entier x = 2\nafficher(x)", "x : Integer := 2;\nPut_Line(x);"),

                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)",
                        "x : Integer := 2;\nPut_Line(\"compteur: \" & x);")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second.strip(), superTest(print.first + "\n").strip());
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("si (x < 5) {\n    afficher(\"x est inférieur à 5\")\n}",
                        "if x < 5 then\n    Put_Line(\"x est inférieur à 5\");\nend if;"),

                Pair.of("si (x > 5) {\n    afficher(\"x est supérieur à 5\")\n} sinon {\n    afficher(\"x est inférieur ou égal à 5\")\n}",
                        "if x > 5 then\n    Put_Line(\"x est supérieur à 5\");\nelse\n    Put_Line(\"x est inférieur ou égal à 5\");\nend if;"),

                Pair.of("si (x < 0) {\n    afficher(\"x est négatif\")\n} snsi (x == 0) {\n    afficher(\"x est nul\")\n} sinon {\n    afficher(\"x est positif\")\n}",
                        "if x < 0 then\n    Put_Line(\"x est négatif\");\nelsif x == 0 then\n    Put_Line(\"x est nul\");\nelse\n    Put_Line(\"x est positif\");\nend if;")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second.strip(), superTest(condition.first + "\n").strip());
        }
    }

    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("tantque (x < 15) {\n    afficher(x)\n    x = x + 1\n}",
                        "while x < 15 loop\n    Put_Line(x);\n    x := x + 1;\nend loop;"),

                Pair.of("faire {\n    afficher(\"Exécution au moins une fois\")\n    y = y - 1\n} tantque (y > 0)",
                        "loop\n    Put_Line(\"Exécution au moins une fois\");\n    y := y - 1;\n    exit when not (y > 0);\nend loop;"),

                Pair.of("pour (x = 0; x < 5; x = x + 1) {\n    afficher(\"compteur: \" & x)\n}",
                        "declare\n    x : Integer := 0;\nbegin\n    for x in 0 .. (5 - 1) loop\n        Put_Line(\"compteur: \" & x);\n        x := x + 1;\n    end loop;\nend;")
        );

        for (Pair<String, String> loop : loops) {
            assertEquals(loop.second.strip(), superTest(loop.first + "\n").strip());
        }
    }

    @Test
    void expressionsTest() {
        List<Pair<String, String>> expressions = List.of(
                //Pair.of("=", ":="),
                Pair.of("+", "+"),
                Pair.of("-", "-"),
                Pair.of("*", "*"),
                Pair.of("^", "**"), // Note: Ada uses "**" for power
                Pair.of("/", "/"),
                //Pair.of("//", "/"), // Ada has no true floor division operator
                Pair.of("%", "%"),
                Pair.of(">", ">"),
                Pair.of("<", "<"),
                Pair.of(">=", ">="),
                Pair.of("<=", "<="),
                Pair.of("==", "=="),
                Pair.of("!=", "!="),
                Pair.of("&", "&") // Ada uses & for string concatenation
        );

        for (Pair<String, String> expression : expressions) {
            String input = "a " + expression.first + " b";
            String expected = "a " + expression.second + " b";
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }

        List<Pair<String, String>> increments = List.of(
                Pair.of("++", " := a + 1;"),
                Pair.of("--", " := a - 1;")
        );

        for (Pair<String, String> increment : increments) {
            String input = "a" + increment.first;
            String expected = "a" + increment.second;
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }
    }

    @Test
    void methodsTest() {
        List<Pair<String, String>> methods = List.of(
                Pair.of("methode vide test() {}", "procedure test() is\nbegin\n\nend test;"),
                Pair.of("methode entier test(entier a, entier b) {}", "function test(a : in Integer; b : in Integer) return Integer is\nbegin\n\nend test;"),
                Pair.of("a.method()", "a.method()"),
                Pair.of("method()", "method()"),
                Pair.of("method(a)", "method(a)")
        );

        for (Pair<String, String> method : methods) {
            String input = method.first;
            String expected = method.second;
            assertEquals(expected.strip(), superTest(input).strip());
        }
    }

    @Test
    void logicalExprTest() {
        List<Pair<String, String>> expressions = List.of(
                Pair.of("var bool res = a || b", "res : Boolean := a or b;"),
                Pair.of("var bool res = x OR y", "res : Boolean := x or y;"),
                Pair.of("var bool res = a && b", "res : Boolean := a and b;"),
                Pair.of("var bool res = x AND y", "res : Boolean := x and y;"),
                Pair.of("var bool res = !a", "res : Boolean := not a;"),
                Pair.of("var bool res = !(x || y)", "res : Boolean := not (x or y);"),
                Pair.of("var bool res = !a && b", "res : Boolean := not a and b;"),
                Pair.of("var bool res = a || b && !c", "res : Boolean := a or b and not c;")
        );

        for (Pair<String, String> expression : expressions) {
            assertEquals(expression.second.strip(), superTest(expression.first + "\n").strip());
        }
    }

    private static String superTest(String input) {
        return Translate.translateToOther(input, Languages.ADA).replace("with Ada.Text_IO; use Ada.Text_IO;\n\n", "");
    }
}
