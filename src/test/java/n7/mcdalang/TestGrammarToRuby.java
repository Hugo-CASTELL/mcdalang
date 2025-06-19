package n7.mcdalang;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.models.antlr.Translate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGrammarToRuby {

    @Test
    void logicalExprTest() {
        List<Pair<String, String>> expressions = List.of(
                Pair.of("var bool res = x OR y", "res = x || y"),
                Pair.of("var bool res = x AND y", "res = x && y"),
                Pair.of("var bool res = NOT x", "res = !x")
        );

        for (Pair<String, String> expression : expressions) {
            assertEquals(expression.second.strip(), superTest(expression.first + "\n").strip());
        }
    }

    @Test
    void ternaryExprTest() {
        List<Pair<String, String>> expressions = List.of(
                Pair.of("var entier x = a ? b : c", "x = a ? b : c"),
                Pair.of("var flottant y = a + b > 10 ? 1.0 : 0.0", "y = a + b > 10 ? 1.0 : 0.0"),
                Pair.of("var chaine res = ok ? \"oui\" : \"non\"", "res = ok ? \"oui\" : \"non\"")
        );

        for (Pair<String, String> expression : expressions) {
            assertEquals(expression.second.strip(), superTest(expression.first + "\n").strip());
        }
    }

    @Test
    void declarationTest() {
        List<Pair<String, String>> declarations = List.of(
                Pair.of("var entier x = 10", "x = 10"),
                Pair.of("var flottant y = 3.14", "y = 3.14"),
                Pair.of("const chaine nom = \"Alice\"", "nom = \"Alice\""),
                Pair.of("var bool estValide = true", "estValide = true"),
                Pair.of("var char lettre = 'A'", "lettre = 'A'")
        );

        for (Pair<String, String> declaration : declarations) {
            assertEquals(declaration.second.strip(), superTest(declaration.first + "\n").strip());
        }
    }

    @Test
    void afficherTest() {
        List<Pair<String, String>> printStatements = List.of(
                Pair.of("afficher(\"message\")", "puts \"message\""),
                Pair.of("var entier x = 2\nafficher(x)", "x = 2\nputs x"),
                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)", "x = 2\nputs \"compteur: \" + x.to_s")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second.strip(), superTest(print.first + "\n").strip());
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("si (x < 5) {\n    afficher(\"x est inferieur a 5\")\n}",
                        "if x < 5\n    puts \"x est inferieur a 5\"\nend"),
                Pair.of("si (x > 5) {\n    afficher(\"x est superieur a 5\")\n} sinon {\n    afficher(\"x est inferieur ou egal a 5\")\n}",
                        "if x > 5\n    puts \"x est superieur a 5\"\nelse\n    puts \"x est inferieur ou egal a 5\"\nend"),
                Pair.of("si (x < 0) {\n    afficher(\"x est negatif\")\n} snsi (x == 0) {\n    afficher(\"x est nul\")\n} sinon {\n    afficher(\"x est positif\")\n}",
                        "if x < 0\n    puts \"x est negatif\"\nelif x == 0\n    puts \"x est nul\"\nelse\n    puts \"x est positif\"\nend")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second.strip(), superTest(condition.first + "\n").strip());
        }
    }

    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("tantque (x < 15) {\n    afficher(x)\n    x = x + 1\n}",
                        "while x < 15\n    puts x\n    x = x + 1\nend"),
                Pair.of("faire {\n    afficher(\"Exécution\")\n    y = y - 1\n} tantque (y > 0)",
                        "begin\n    puts \"Exécution\"\n    y = y - 1\nend while y > 0"),
                Pair.of("pour (x = 0; x < 5; x = x + 1) {\n    afficher(\"compteur: \" & x)\n}",
                        "for x in 0...5\n    puts \"compteur: \" + x.to_s\nend")
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
                Pair.of("//", "/"),  // Ruby uses `/` for integer division if both operands are integers
                Pair.of("%", "%"),
                Pair.of(">", ">"),
                Pair.of("<", "<"),
                Pair.of(">=", ">="),
                Pair.of("<=", "<="),
                Pair.of("==", "=="),
                Pair.of("!=", "!="),
                Pair.of("&", "+")  // string concatenation
        );
        for (Pair<String, String> expression : expressions) {
            String input = "a " + expression.first + " b";
            String expected = "a " + expression.second + " b";
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }

        List<Pair<String, String>> increments = List.of(
                Pair.of("++", "+= 1"),
                Pair.of("--", "-= 1")
        );
        for (Pair<String, String> increment : increments) {
            String input = "a" + increment.first;
            String expected = "a " + increment.second;
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }
    }

    @Test
    void methodTest() {
        List<Pair<String, String>> methods = List.of(
                Pair.of("methode vide test() {}", "def test\nend"),
                Pair.of("methode entier test(entier a, entier b) {}", "def test(a, b)\nend")
        );

        for (Pair<String, String> method : methods) {
            assertEquals(method.second.strip(), superTest(method.first + "\n").strip());
        }
    }

    @Test
    void mixedTest() {
        List<Pair<String, String>> mixedSnippets = List.of(
                Pair.of("""
                    var entier x = 0
                    tantque (x < 3) {
                        afficher(x)
                        x = x + 1
                    }
                    """,
                        """
                    x = 0
                    while x < 3
                        puts x
                        x = x + 1
                    end
                    """),

                Pair.of("""
                    var entier a = 2
                    var entier b = 3
                    si (a < b) {
                        afficher("a est plus petit que b")
                    } sinon {
                        afficher("a est plus grand ou egal a b")
                    }
                    """,
                        """
                    a = 2
                    b = 3
                    if a < b
                        puts "a est plus petit que b"
                    else
                        puts "a est plus grand ou egal a b"
                    end
                    """),

                Pair.of("""
                    methode vide afficherNombre(entier n) {
                        var entier i
                        pour (i = 0; i < n; i = i + 1) {
                            afficher("Nombre: " & i)
                        }
                    }
                    """,
                        """
                    def afficherNombre(n)
                        i = 0
                        for i in 0...n
                            puts "Nombre: " + i.to_s
                        end
                    end
                    """)
        );

        for (Pair<String, String> snippet : mixedSnippets) {
            assertEquals(snippet.second.strip(), superTest(snippet.first + "\n").strip());
        }
    }

    @Test
    void mathFunctionTests() {
        List<Pair<String, String>> mathSnippets = List.of(
                Pair.of("""
                    methode entier factoriel(entier n) {
                        var entier resultat = 1
                        var entier i
                        pour (i = 1; i <= n; i = i + 1) {
                            resultat = resultat * i
                        }
                        return resultat
                    }
                    """,
                        """
                    def factoriel(n)
                        resultat = 1
                        i = 1
                        for i in 1..n
                            resultat = resultat * i
                        end
                        return resultat
                    end
                    """),

                Pair.of("""
                    methode entier pgcd(entier a, entier b) {
                        tantque (b != 0) {
                            var entier temp = b
                            b = a % b
                            a = temp
                        }
                        return a
                    }
                    """,
                        """
                    def pgcd(a, b)
                        while b != 0
                            temp = b
                            b = a % b
                            a = temp
                        end
                        return a
                    end
                    """),

                Pair.of("""
                    methode entier fibonacci(entier n) {
                        si (n <= 1) {
                            return n
                        }
                        return fibonacci(n - 1) + fibonacci(n - 2)
                    }
                    """,
                        """
                    def fibonacci(n)
                        if n <= 1
                            return n
                        end
                        return fibonacci(n - 1) + fibonacci(n - 2)
                    end
                    """)
        );

        for (Pair<String, String> snippet : mathSnippets) {
            assertEquals(snippet.second.strip(), superTest(snippet.first + "\n").strip());
        }
    }

    private static String superTest(String input) {
        return Translate.translateToOther(input, Languages.RUBY);
    }
}
