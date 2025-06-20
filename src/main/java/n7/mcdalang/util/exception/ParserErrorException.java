package n7.mcdalang.util.exception;

import java.util.List;

public class ParserErrorException extends RuntimeException {
    private final List<String> stack;
    private final Object offendingSymbol;
    private final int line;
    private final int charPositionInLine;

    private int var;

    public ParserErrorException(List<String> stack, int line, int charPositionInLine, Object offendingSymbol) {
        this.stack = stack;
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.offendingSymbol = offendingSymbol;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder("Parser Error :\n");

        message.append(indent(4) + "Line : ").append(line).append("\n")
                .append(indent(4) + "Character Position : ").append(charPositionInLine).append("\n")
                .append(indent(4) + "Stack Trace :\n");

        for (String stackElement : stack) {
            message.append(indent(8)).append(stackElement).append("\n");
        }

        return message.toString();
    }

    private String indent(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
        return  sb.toString();
    }
}
