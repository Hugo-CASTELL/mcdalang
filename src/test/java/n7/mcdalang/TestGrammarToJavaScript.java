package n7.mcdalang;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.models.antlr.Translate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGrammarToJavaScript {

    @Test
    void logicalExprTest() {
        List<Pair<String, String>> expressions = List.of(
                Pair.of("var bool res = x OR y", "let res = x || y;"),
                Pair.of("var bool res = x AND y", "let res = x && y;"),
                Pair.of("var bool res = NOT x", "let res = null;")
        );

        for (Pair<String, String> expression : expressions) {
            assertEquals(expression.second.strip(), superTest(expression.first + "\n").strip());
        }
    }

    @Test
    void ternaryExprTest() {
        List<Pair<String, String>> expressions = List.of(
                Pair.of("var entier x = a ? b : c", "let x = (a ? b : c);"),
                Pair.of("var flottant y = a + b > 10 ? 1.0 : 0.0", "let y = (a + b > 10 ? 1.0 : 0.0);"),
                Pair.of("var chaine res = ok ? \"oui\" : \"non\"", "let res = (ok ? \"oui\" : \"non\");")
        );

        for (Pair<String, String> expression : expressions) {
            assertEquals(expression.second.strip(), superTest(expression.first + "\n").strip());
        }
    }

    @Test
    void declarationTest() {
        List<Pair<String, String>> declarations = List.of(
                Pair.of("var entier x = 10", "let x = 10;"),
                Pair.of("var flottant y = 3.14", "let y = 3.14;"),
                Pair.of("const chaine nom = \"Alice\"", "const nom = \"Alice\";"),
                Pair.of("var bool estValide = true", "let estValide = true;"),
                Pair.of("var char lettre = 'A'", "let lettre = 'A';")
        );

        for (Pair<String, String> declaration : declarations) {
            assertEquals(declaration.second.strip(), superTest(declaration.first + "\n").strip());
        }
    }

    @Test
    void afficherTest() {
        List<Pair<String, String>> printStatements = List.of(
                Pair.of("afficher(\"message\")", "console.log(\"message\");"),
                Pair.of("var entier x = 2\nafficher(x)", "let x = 2;\nconsole.log(x);"),
                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)", "let x = 2;\nconsole.log(\"compteur: \" + x);")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second.strip(), superTest(print.first + "\n").strip());
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("si (x < 5) {\n    afficher(\"x est inferieur a 5\")\n}",
                        "if (x < 5) {\n    console.log(\"x est inferieur a 5\");\n}"),
                Pair.of("si (x > 5) {\n    afficher(\"x est superieur a 5\")\n} sinon {\n    afficher(\"x est inferieur ou egal a 5\")\n}",
                        "if (x > 5) {\n    console.log(\"x est superieur a 5\");\n} else {\n    console.log(\"x est inferieur ou egal a 5\");\n}"),
                Pair.of("si (x < 0) {\n    afficher(\"x est negatif\")\n} snsi (x == 0) {\n    afficher(\"x est nul\")\n} sinon {\n    afficher(\"x est positif\")\n}",
                        "if (x < 0) {\n    console.log(\"x est negatif\");\n} else if (x == 0) {\n    console.log(\"x est nul\");\n} else {\n    console.log(\"x est positif\");\n}")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second.strip(), superTest(condition.first + "\n").strip());
        }
    }

    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("tantque (x < 15) {\n    afficher(x)\n    x = x + 1\n}",
                        "while (x < 15) {\n    console.log(x);\n    x = x + 1;\n}"),
                Pair.of("faire {\n    afficher(\"Exécution\")\n    y = y - 1\n} tantque (y > 0)",
                        "do {\n    console.log(\"Exécution\");\n    y = y - 1;\n} while (y > 0);"),
                Pair.of("pour (x = 0; x < 5; x = x + 1) {\n    afficher(\"compteur: \" + x)\n}",
                        "for (x = 0; x < 5; x = x + 1) {\n    console.log(\"compteur: \" + x);\n}")
        );

        for (Pair<String, String> loop : loops) {
            assertEquals(loop.second.strip(), superTest(loop.first).strip());
        }
    }

    @Test
    void expressionsTest() {
        List<Pair<String, String>> expressions = List.of(
                Pair.of("+", "+"),
                Pair.of("-", "-"),
                Pair.of("*", "*"),
                Pair.of("/", "/"),
                //Pair.of("//", "//"), // maybe needs comment or integer division handler
                Pair.of("%", "%"),
                Pair.of(">", ">"),
                Pair.of("<", "<"),
                Pair.of(">=", ">="),
                Pair.of("<=", "<="),
                Pair.of("==", "=="),
                Pair.of("!=", "!="),
                Pair.of("&", "+")
        );

        for (Pair<String, String> expression : expressions) {
            String input = "a " + expression.first + " b";
            String expected = "a " + expression.second + " b";
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }

        List<Pair<String, String>> increments = List.of(
                Pair.of("++", " += 1"),
                Pair.of("--", " -= 1")
        );

        for (Pair<String, String> increment : increments) {
            String input = "a" + increment.first;
            String expected = "a" + increment.second + ";";
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }
    }

    @Test
    void methodTest() {
        List<Pair<String, String>> methods = List.of(
                Pair.of("methode vide test() {}", "function test() {\n\n}"),
                Pair.of("methode entier test(entier a, entier b) {\n}", "function test(a, b) {\n\n}")
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
                let x = 0;
                while (x < 3) {
                    console.log(x);
                    x = x + 1;
                }
                
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
                let a = 2;
                let b = 3;
                if (a < b) {
                    console.log("a est plus petit que b");
                } else {
                    console.log("a est plus grand ou egal a b");
                }
                
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
                function afficherNombre(n) {
                    let i;
                    for (i = 0; i < n; i = i + 1) {
                        console.log("Nombre: " + i);
                    }
                }
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
                function factoriel(n) {
                    let resultat = 1;
                    let i;
                    for (i = 1; i <= n; i = i + 1) {
                        resultat = resultat * i;
                    }
                    return resultat;
                }
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
                function pgcd(a, b) {
                    while (b != 0) {
                        let temp = b;
                        b = a % b;
                        a = temp;
                    }
                    return a;
                }
                """),

                Pair.of("""
                methode entier fibonacci(entier n) {
                    si (n <= 1) {
                        return n
                    }sinon{
                        return fibonacci(n - 1) + fibonacci(n - 2)
                    }
                }
                """,
                        """
                function fibonacci(n) {
                    if (n <= 1) {
                        return n;
                    } else {
                        return fibonacci(n - 1) + fibonacci(n - 2);
                    }
                }
                """)
        );

        for (Pair<String, String> snippet : mathSnippets) {
            assertEquals(snippet.second.strip(), superTest(snippet.first + "\n").strip());
        }
    }

    private static String superTest(String input) {
        return Translate.translateToOther(input, Languages.JavaScript); // Ensure Languages.JAVASCRIPT is implemented
    }
}
