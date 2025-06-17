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
                Pair.of("afficher(\"message\")", "printf(\"%s\\n\", \"message\");"),
                Pair.of("var entier x = 2\nafficher(x)", "int x = 2;\nprintf(\"%s\\n\", x);"),
                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)", "int x = 2;\nprintf(\"%s\\n\", \"compteur: \" + x);")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second.strip(), superTest(print.first + "\n").strip());
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("si (x < 5) {\n    afficher(\"x est inferieur a 5\")\n}",
                        "if (x < 5) {\n    printf(\"%s\\n\", \"x est inferieur a 5\");\n}"),
                Pair.of("si (x > 5) {\n    afficher(\"x est superieur a 5\")\n} sinon {\n    afficher(\"x est inferieur ou egal a 5\")\n}",
                        "if (x > 5) {\n    printf(\"%s\\n\", \"x est superieur a 5\");\n} else {\n    printf(\"%s\\n\", \"x est inferieur ou egal a 5\");\n}"),
                Pair.of("si (x < 0) {\n    afficher(\"x est negatif\")\n} snsi (x == 0) {\n    afficher(\"x est nul\")\n} sinon {\n    afficher(\"x est positif\")\n}",
                        "if (x < 0) {\n    printf(\"%s\\n\", \"x est negatif\");\n} else if (x == 0) {\n    printf(\"%s\\n\", \"x est nul\");\n} else {\n    printf(\"%s\\n\", \"x est positif\");}")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second.strip(), superTest(condition.first + "\n").strip());
        }
    }

    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("tantque (x < 15) {\n    afficher(x)\n    x = x + 1\n}",
                        "while (x < 15) {\n    printf(\"%s\\n\", x);\n    x = x + 1;\n}"),
                Pair.of("faire {\n    afficher(\"Exécution\")\n    y = y - 1\n} tantque (y > 0)",
                        "do {\n    printf(\"%s\\n\", \"Exécution\");\n    y = y - 1;\n} while (y > 0);"),
                Pair.of("pour (x = 0; x < 5; x = x + 1) {\n    afficher(\"compteur: \" & x)\n}",
                        "for (x = 0; x < 5; x = x + 1) {\n    printf(\"%s\\n\", \"compteur: \" + x);\n}")
        );

        for (Pair<String, String> loop : loops) {
            assertEquals(loop.second.strip(), superTest(loop.first + "\n").strip());
        }
    }

    @Test
    void expressionsTest() {
        // Expressions between two variables (assignation included)
        List<Pair<String, String>> expressions = List.of(
                //Pair.of("=", "="),
                Pair.of("+", "+"),
                Pair.of("-", "-"),
                Pair.of("*", "*"),
                //Pair.of("^", "**"),
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
            //expected = expected + ";";
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }

        // Expressions with a constant
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
                Pair.of("methode vide test() {}", "void test() {\n\n}"),
                Pair.of("methode entier test(entier a, entier b) {}", "int test(int a, int b) {\n\n}")
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
                    int x = 0;
                    while (x < 3) {
                        printf("%s\\n", x);
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
                        int a = 2;
                        int b = 3;
                        if (a < b) {
                            printf("%s\\n", "a est plus petit que b");
                        } else {
                            printf("%s\\n", "a est plus grand ou egal a b");
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
                    void afficherNombre(int n) {
                        int i;
                        for (i = 0; i < n; i = i + 1) {
                            printf("%s\\n", "Nombre: " + i);
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
                // Factorielle
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
                        int factoriel(int n) {
                            int resultat = 1;
                            int i;
                            for (i = 1; i <= n; i = i + 1) {
                                resultat = resultat * i;
                            }
                            return resultat;
                        }
                        """),

                // PGCD (algorithme d'Euclide)
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
                        int pgcd(int a, int b) {
                            while (b != 0) {
                                int temp = b;
                                b = a % b;a = temp;
                            }
                            return a;
                        }
                        """),

                // Fibonacci
                Pair.of("""
                    methode entier fibonacci(entier n) {
                        si (n <= 1) {
                            return n
                        }
                        return fibonacci(n - 1) + fibonacci(n - 2)
                    }
                    """,
                        """
                        int fibonacci(int n) {
                            if (n <= 1) {
                                return n;
                            }
                            return fibonacci(n - 1) + fibonacci(n - 2);
                        }
                        """)
        );

        for (Pair<String, String> snippet : mathSnippets) {
            assertEquals(snippet.second.strip(), superTest(snippet.first + "\n").strip());
        }
    }



    private static String superTest(String input) {
        return Translate.translateToOther(input, Languages.C).replace("#include <stdio.h>\n", "");
    }
}
