package n7.mcdalang.models.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class Translate {

    public static String translateToOther(String inputText) throws IOException {
        // Utiliser CharStreams au lieu de ANTLRInputStream
        CharStream input = CharStreams.fromString(inputText);

        McdalangLexer lexer = new McdalangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        McdalangParser parser = new McdalangParser(tokens);

        // How to incerept error to display it ?
        ParseTree tree = parser.prog();

        // Traduction avec listener
        ParseTreeWalker walker = new ParseTreeWalker();


        /* les langages **/
        McdalangToC translatorToC = new McdalangToC();
        McdalangToPython translatorToPython = new McdalangToPython();
        McdalangToRust translatorToRust = new McdalangToRust();
        McdalangToAda translatorToAda = new McdalangToAda();
        McdalangToJava translatorToJava = new McdalangToJava();
        McdalangToGo translatorToGo = new McdalangToGo();
        McdalangToCPlusPlus translatorToCPlusPlus = new McdalangToCPlusPlus();
        McdalangToPHP translatorToPHP = new McdalangToPHP();


        walker.walk(translatorToPython, tree);
        walker.walk(translatorToC, tree);

        // On fera un enum pouur que ce soit au choix

        return translatorToPython.getPythonCode();

    }
}
