package n7.mcdalang.models.antlr;

import n7.mcdalang.util.exception.ParserErrorException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class Translate {

    public static String translateToOther(String inputText, Languages languages) throws ParserErrorException {
        // Utiliser CharStreams au lieu de ANTLRInputStream
        CharStream input = CharStreams.fromString(inputText);

        McdalangLexer lexer = new McdalangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        McdalangParser parser = new McdalangParser(tokens);

        parser.addErrorListener(new FetchAntlrError());

        // How to incerept error to display it ?
        ParseTree tree = parser.prog();

        // Traduction avec listener
        ParseTreeWalker walker = new ParseTreeWalker();

        OutputBaseListener translator = null;

        switch (languages) {
            case JAVA -> translator = new McdalangToJava();
            case C ->  translator = new McdalangToC();
            case POWERSHELL -> translator = new McdalangToPowershell();
            case ADA -> translator = new McdalangToAda();
            case ASSEMBLY ->  translator = new McdalangToAssembly();
            case PYTHON ->  translator = new McdalangToPython();
            case RUST ->  translator = new McdalangToRust();
            case GO ->  translator = new McdalangToGo();
            case CPlusPlus ->  translator = new McdalangToCPlusPlus();

            default -> new RuntimeException("Language not supported");
        }

        walker.walk(translator, tree);
        return translator.getCode();
    }
}
