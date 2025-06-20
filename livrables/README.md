# ANTLR
1) Creer un fichier Expr.g4 (dans le dossier ***src/main/antlr***)
Exemple :
```
grammar Expr;

expr: INT '+' INT ;       // Une expression simple : ex. 1 + 2

INT : [0-9]+ ;            // Un entier
WS  : [ \t\r\n]+ -> skip; // Ignorer les espaces blancs
`````
2) Taper dans le cmd: ```./gradlew generateGrammarSource```
3) Taper dans le cmd ```./gradlew build```
4) Executer votre code qui utilise antlr (dans le dossier ***src/main/java***). exemple
```
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Test {
    public static void main(String[] args) {
        String input = "3 + 4";
        CharStream charStream = CharStreams.fromString(input);
        ExprLexer lexer = new ExprLexer(charStream);        // généré par ANTLR
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);         // généré par ANTLR

        ParseTree tree = parser.expr();                     // lance le parsing

        System.out.println("Arbre de syntaxe : " + tree.toStringTree(parser));
    }
}
```