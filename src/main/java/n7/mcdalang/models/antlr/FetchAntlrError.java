package n7.mcdalang.models.antlr;

import n7.mcdalang.util.exception.ParserErrorException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class FetchAntlrError extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e)
    {
        List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        StringBuilder buf = new StringBuilder();
        buf.append("rule stack: "+stack+" ");
        buf.append("line "+line+":"+charPositionInLine+" at "+
                offendingSymbol+": "+msg);

        throw new ParserErrorException(stack, line, charPositionInLine, offendingSymbol);
    }
}


