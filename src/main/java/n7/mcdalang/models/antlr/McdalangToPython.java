package n7.mcdalang.models.antlr;

import n7.mcdalang.models.antlr.OutputBaseListener;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;

public class McdalangToPython extends OutputBaseListener {
    public McdalangToPython() {
        output = new StringBuilder();
    }
    ParseTreeProperty<String> values = new ParseTreeProperty<>();

    private String indent(String code) {
        if (code == null || code.isBlank()) {
            return "";
        }
        // Split the code into lines, filter out any blank lines,
        // prepend the indentation, and then join them back together with newlines.
        return Arrays.stream(code.split("\n"))
                .filter(line -> !line.isBlank())
                .map(line -> "    " + line)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void exitProg(McdalangParser.ProgContext ctx) {
        for (var stmt : ctx.statement()) {
            String val = values.get(stmt);
            if (val != null) output.append(val);
        }
    }

    @Override
    public void exitStatement(McdalangParser.StatementContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            String val = values.get(ctx.getChild(i));
            if (val != null) {
                values.put(ctx, val);
                return;
            }
        }
    }

    @Override
    public void exitVarDecl(McdalangParser.VarDeclContext ctx) {
        String id = ctx.ID().getText();
        if (ctx.expr() != null) {
            String expr = values.get(ctx.expr());
            values.put(ctx, id + " = " + expr + "\n");
        } else {
            values.put(ctx, "");
        }
    }

    @Override
    public void exitAssignment(McdalangParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        String expr = values.get(ctx.expr());
        values.put(ctx, id + " = " + expr);
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "print(" + expr + ")\n");
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String methodName = ctx.ID().getText();
        String params = "";

        if (ctx.paramList() != null) {
            params = String.join(", ", ctx.paramList().ID().stream()
                    .map(ParseTree::getText)
                    .toList());
        }

        String body = values.get(ctx.block());
        values.put(ctx, "def " + methodName + "(" + params + "):\n" + indent(body));
    }

    @Override
    public void exitReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "return " + expr + "\n");
    }

    @Override
    public void exitFuncCall(McdalangParser.FuncCallContext ctx) {
        List<String> callers = new ArrayList<>();
        List<String> args = new ArrayList<>();
        for (var arg : ctx.expr()) {
            args.add(values.get(arg));
        }
        for (var caller : ctx.ID()) {
            callers.add(caller.getText());
        }
        values.put(ctx, String.join(".", callers) + "(" + String.join(", ", args) + ")");
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder result = new StringBuilder();
        if (ctx.getText().contains("snsi")) {
            // si
            String cond1 = values.get(ctx.expr(0));
            String block1 = values.get(ctx.block(0));
            result.append("if ").append(cond1).append(":\n").append(indent(block1));

            // snsi
            String cond2 = values.get(ctx.expr(1));
            String block2 = values.get(ctx.block(1));
            result.append("\nelif ").append(cond2).append(":\n").append(indent(block2));

            // sinon (optionnel)
            if (ctx.block().size() == 3) {
                String block3 = values.get(ctx.block(2));
                result.append("\nelse:\n").append(indent(block3));
            }

            result.append("\n");
        } else {
            // Cas simple : si + (sinon) ?
            String cond = values.get(ctx.expr(0));
            String block = values.get(ctx.block(0));
            result.append("if ").append(cond).append(":\n").append(indent(block));

            if (ctx.block().size() == 2) {
                String elseBlock = values.get(ctx.block(1));
                result.append("\nelse:\n").append(indent(elseBlock));
            }

            result.append("\n");
        }

        values.put(ctx, result.toString());
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result = "";
        if (ctx.getStart().getText().equals("tantque")) {
            String cond = values.get(ctx.expr());
            String body = values.get(ctx.block());
            result = "while " + cond + ":\n" + indent(body) + "\n";
        } else if (ctx.getStart().getText().equals("faire")) {
            String body = values.get(ctx.block());
            String cond = values.get(ctx.expr());
            result = "while True:\n" + indent(body) + "\n" + indent("if not (" + cond + "): break\n") + "\n";
        } else { // pour
            String init = values.get(ctx.assignment(0)).strip();
            String cond = values.get(ctx.expr()).strip();
            String update = values.get(ctx.assignment(1)).strip();

            String[] parts = init.split("=");
            String var = parts[0].strip();
            String start = parts[1].strip();
            String end = cond.replaceAll(".*[<]=?\\s*", "").strip();
            boolean inclusive = cond.contains("<=");

            if (inclusive) {
                end += " + 1";
            }

            String body = values.get(ctx.block());

            result = "for " + var + " in range(" + start + ", " + end + "):\n" + indent(body) + "\n";
        }
        values.put(ctx, result);
    }

    @Override
    public void exitBlock(McdalangParser.BlockContext ctx) {
        StringBuilder sb = new StringBuilder();
        for (var stmt : ctx.statement()) {
            String val = values.get(stmt);
            if (val != null) sb.append(val);
        }
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitExpr(McdalangParser.ExprContext ctx) {
        values.put(ctx, values.get(ctx.concatenationExpr()));
    }

    @Override
    public void exitConcatenationExpr(McdalangParser.ConcatenationExprContext ctx) {
        if (ctx.equalityExpr().size() == 1) {
            values.put(ctx, values.get(ctx.equalityExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder(values.get(ctx.equalityExpr(0)));
            for (int i = 1; i < ctx.equalityExpr().size(); i++) {
                sb.append(" + ").append(values.get(ctx.equalityExpr(i)));
            }
            values.put(ctx, sb.toString());
        }
    }

    @Override
    public void exitEqualityExpr(McdalangParser.EqualityExprContext ctx) {
        if (ctx.relationalExpr().size() == 1) {
            values.put(ctx, values.get(ctx.relationalExpr(0)));
        } else {
            String left = values.get(ctx.relationalExpr(0));
            String right = values.get(ctx.relationalExpr(1));
            String op = ctx.getChild(1).getText();
            values.put(ctx, left + " " + op + " " + right);
        }
    }

    @Override
    public void exitRelationalExpr(McdalangParser.RelationalExprContext ctx) {
        if (ctx.addExpr().size() == 1) {
            values.put(ctx, values.get(ctx.addExpr(0)));
        } else {
            String left = values.get(ctx.addExpr(0));
            String right = values.get(ctx.addExpr(1));
            String op = ctx.getChild(1).getText();
            values.put(ctx, left + " " + op + " " + right);
        }
    }

    @Override
    public void exitAddExpr(McdalangParser.AddExprContext ctx) {
        if (ctx.mulExpr().size() == 1) {
            values.put(ctx, values.get(ctx.mulExpr(0)));
        } else {
            String left = values.get(ctx.mulExpr(0));
            String right = values.get(ctx.mulExpr(1));
            String op = ctx.getChild(1).getText();
            values.put(ctx, left + " " + op + " " + right);
        }
    }

    @Override
    public void exitMulExpr(McdalangParser.MulExprContext ctx) {
        if (ctx.powExpr().size() == 1) {
            values.put(ctx, values.get(ctx.powExpr(0)));
        } else {
            String left = values.get(ctx.powExpr(0));
            String right = values.get(ctx.powExpr(1));
            String op = ctx.getChild(1).getText();
            values.put(ctx, left + " " + op + " " + right);
        }
    }

    @Override
    public void exitPowExpr(McdalangParser.PowExprContext ctx) {
        if (ctx.atom().size() == 1) {
            values.put(ctx, values.get(ctx.atom(0)));
        } else {
            String left = values.get(ctx.atom(0));
            String right = values.get(ctx.atom(1));
            values.put(ctx, left + " ** " + right);
        }
    }

    @Override
    public void exitIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String var = ctx.ID().getText();
        String op = ctx.getChild(1).getText(); // ++ ou --
        String result = "";
        if (op.equals("++")) {
            result = var + " = " + var + " + 1\n";
        } else if (op.equals("--")) {
            result = var + " = " + var + " - 1\n";
        }
        values.put(ctx, result);
    }


    @Override
    public void exitAtom(McdalangParser.AtomContext ctx) {
        if (ctx.INT() != null) values.put(ctx, ctx.INT().getText());
        else if (ctx.FLOAT() != null) values.put(ctx, ctx.FLOAT().getText());
        else if (ctx.STRING() != null) values.put(ctx, ctx.STRING().getText());
        else if (ctx.CHAR() != null) values.put(ctx, ctx.CHAR().getText());
        else if (ctx.ID() != null) values.put(ctx, ctx.ID().getText());
        else if (ctx.funcCall() != null) values.put(ctx, values.get(ctx.funcCall()));
        else if (ctx.expr() != null) values.put(ctx, "(" + values.get(ctx.expr()) + ")");
        else if (ctx.getStart().getText().equals("true")) values.put(ctx, "True");
        else if (ctx.getStart().getText().equals("false")) values.put(ctx, "False");
    }
}
