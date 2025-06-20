package n7.mcdalang;

import com.ibm.icu.impl.Pair;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.models.antlr.Translate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGrammarToPowershell {

    @Test
    void declarationTest() {
        List<Pair<String, String>> declarations = List.of(
                Pair.of("var entier x = 10", "$x = 10"),
                Pair.of("var flottant y = 3.14", "$y = 3.14"),
                Pair.of("const chaine nom = \"Alice\"", "$nom = \"Alice\""),
                Pair.of("var bool estValide = true", "$estValide = $true"),
                Pair.of("var char lettre = 'A'", "$lettre = 'A'")
        );

        for (Pair<String, String> declaration : declarations) {
            assertEquals(declaration.second.strip(), superTest(declaration.first + "\n").strip());
        }
    }

    @Test
    void afficherTest() {
        List<Pair<String, String>> printStatements = List.of(
                Pair.of("afficher(\"message\")", "Write-Output \"message\""),
                Pair.of("var entier x = 2\nafficher(x)", "$x = 2\nWrite-Output $x"),
                Pair.of("var entier x = 2\nafficher(\"compteur: \" & x)", "$x = 2\nWrite-Output (\"compteur: \" + $x)")
        );

        for (Pair<String, String> print : printStatements) {
            assertEquals(print.second.strip(), superTest(print.first + "\n").strip());
        }
    }

    @Test
    void conditionTest() {
        List<Pair<String, String>> conditions = List.of(
                Pair.of("""
                        si (x < 5) {
                            afficher("x est inferieur a 5")
                        }""",
                        """
                        if ($x -lt 5) {
                            Write-Output "x est inferieur a 5"
                        }"""),

                Pair.of("""
                        si (x > 5) {
                            afficher("x est superieur a 5")
                        } sinon {
                            afficher("x est inferieur ou egal a 5")
                        }""",
                        """
                        if ($x -gt 5) {
                            Write-Output "x est superieur a 5"
                        } else {
                            Write-Output "x est inferieur ou egal a 5"
                        }"""),

                Pair.of("""
                        si (x < 0) {
                            afficher("x est negatif")
                        } snsi (x == 0) {
                            afficher("x est nul")
                        } sinon {
                            afficher("x est positif")
                        }""",
                        """
                        if ($x -lt 0) {
                            Write-Output "x est negatif"
                        } elseif ($x -eq 0) {
                            Write-Output "x est nul"
                        } else {
                            Write-Output "x est positif"
                        }""")
        );

        for (Pair<String, String> condition : conditions) {
            assertEquals(condition.second.strip(), superTest(condition.first + "\n").strip());
        }
    }

    @Test
    void boucleTest() {
        List<Pair<String, String>> loops = List.of(
                Pair.of("""
                        tantque (x < 15) {
                            afficher(x)
                            x = x + 1
                        }""",
                        """
                        while ($x -lt 15) {
                            Write-Output $x
                            $x = ($x + 1)
                        }"""),

                Pair.of("""
                        faire {
                            afficher("Exécution")
                            y = y - 1
                        } tantque (y > 0)""",
                        """
                        do {
                            Write-Output "Exécution"
                            $y = ($y - 1)
                        } while ($y -gt 0)"""),

                Pair.of("""
                        pour (x = 0; x < 5; x = x + 1) {
                            afficher("compteur: " & x)
                        }""",
                        """
for ($x = 0; ($x -lt 5); $x = ($x + 1)) {
    Write-Output ("compteur: " + $x)
}""")
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
                Pair.of("%", "%"),
                Pair.of(">", "-gt"),
                Pair.of("<", "-lt"),
                Pair.of(">=", "-ge"),
                Pair.of("<=", "-le"),
                Pair.of("==", "-eq"),
                Pair.of("!=", "-ne"),
                Pair.of("&", "+") // if & is treated as concatenation
        );

        for (Pair<String, String> expression : expressions) {
            String input = "a " + expression.first + " b";
            String expected = "($a " + expression.second + " $b)";
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }

        List<Pair<String, String>> increments = List.of(
                Pair.of("++", " += 1"),
                Pair.of("--", " -= 1")
        );

        for (Pair<String, String> increment : increments) {
            String input = "a" + increment.first;
            String expected = "$a" + increment.second;
            assertEquals(expected.strip(), superTest(input + "\n").strip());
        }
    }

    @Test
    void methodTest() {
        List<Pair<String, String>> methods = List.of(
                Pair.of("methode vide test() {}", "function test {\n}"),
                Pair.of("methode entier test(entier a, entier b) {}", "function test($a, $b) {\n}")
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
                        }""",
                        """
                        $x = 0
                        while ($x -lt 3) {
                            Write-Output $x
                            $x = ($x + 1)
                        }"""),

                Pair.of("""
                        var entier a = 2
                        var entier b = 3
                        si (a < b) {
                            afficher("a est plus petit que b")
                        } sinon {
                            afficher("a est plus grand ou egal a b")
                        }""",
                        """
                        $a = 2
                        $b = 3
                        if ($a -lt $b) {
                            Write-Output "a est plus petit que b"
                        } else {
                            Write-Output "a est plus grand ou egal a b"
                        }"""),

                Pair.of("""
                        methode vide afficherNombre(entier n) {
                            var entier i
                            pour (i = 0; i < n; i = i + 1) {
                                afficher("Nombre: " & i)
                            }
                        }""",
                        """
function afficherNombre($n) {
    $i = $null
    for ($i = 0; ($i -lt $n); $i = ($i + 1)) {
        Write-Output ("Nombre: " + $i)
    }
}""")
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
                        }""",
                        """
function factoriel($n) {
    $resultat = 1
    $i = $null
    for ($i = 1; ($i -le $n); $i = ($i + 1)) {
        $resultat = ($resultat * $i)
    }
    return $resultat
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
                        }""",
                        """
                        function pgcd($a, $b) {
                            while ($b -ne 0) {
                                $temp = $b
                                $b = ($a % $b)
                                $a = $temp
                            }
                            return $a
                        }"""),

                Pair.of("""
                        methode entier fibonacci(entier n) {
                            si (n <= 1) {
                                return n
                            }sinon{
                                return fibonacci(n - 1) + fibonacci(n - 2)
                            }
                        }""",
                        """
function fibonacci($n) {
    if ($n -le 1) {
        return $n
    } else {
        return ((fibonacci(($n - 1)) + fibonacci(($n - 2))))
    }
}""")
        );

        for (Pair<String, String> snippet : mathSnippets) {
            assertEquals(snippet.second.strip(), superTest(snippet.first + "\n").strip());
        }
    }

    private static String superTest(String input) {
        return Translate.translateToOther(input, Languages.POWERSHELL);
    }
}
