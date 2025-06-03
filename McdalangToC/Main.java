import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: java Main <fichier.mcdalang>");
            return;
        }

        // Lecture du fichier source
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        McdalangLexer lexer = new McdalangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        McdalangParser parser = new McdalangParser(tokens);

        // Parsing et traduction
        ParseTree tree = parser.prog();
        ParseTreeWalker walker = new ParseTreeWalker();
        McdalangToC translator = new McdalangToC();

        walker.walk(translator, tree);

        // Affichage du code C généré
        System.out.println(translator.getCCode());
    }
}

