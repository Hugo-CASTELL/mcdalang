package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;

public class McdalangToRuby extends OutputBaseListener {

    public McdalangToRuby() {
        output = new StringBuilder();
    }

    ParseTreeProperty<String> values = new ParseTreeProperty<>();

    private String indent(String code) {
        if (code == null || code.isBlank()) return "";
        return Arrays.stream(code.split("\n"))
                .filter(line -> !line.isBlank())
                .map(line -> "  " + line)
                .collect(Collectors.joining("\n"));
    }

    private String translateType(String type) {
        return ""; // Ruby does not use types in declarations
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
        if (ctx.NEWLINE() != null && ctx.getChildCount() == 1) {
            values.put(ctx, "\n");
            return;
        }
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
            values.put(ctx, id + " = nil\n");
        }
    }

    @Override
    public void exitAssignment(McdalangParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        String expr = values.get(ctx.expr());
        values.put(ctx, id + " = " + expr + "\n");
    }

    @Override
    public void exitIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String var = ctx.ID().getText();
        String op = ctx.getChild(1).getText();
        if (op.equals("++")) {
            values.put(ctx, var + " += 1\n");
        } else {
            values.put(ctx, var + " -= 1\n");
        }
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "puts " + expr + "\n");
    }

    @Override
    public void exitReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "return " + expr + "\n");
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String methodName = ctx.ID().getText();
        String params = "";

        if (ctx.paramList() != null) {
            var ids = ctx.paramList().ID();
            List<String> paramNames = ids.stream().map(ParseTree::getText).toList();
            params = String.join(", ", paramNames);
        }

        String body = values.get(ctx.block());
        values.put(ctx, "def " + methodName + "(" + params + ")\n" + indent(body) + "\nend\n");
    }

    @Override
    public void exitFuncCall(McdalangParser.FuncCallContext ctx) {
        List<String> callers = new ArrayList<>();
        List<String> args = new ArrayList<>();
        for (var expr : ctx.expr()) args.add(values.get(expr));
        for (var caller : ctx.ID()) callers.add(caller.getText());
        values.put(ctx, String.join("_", callers) + "(" + String.join(", ", args) + ")");
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append("if ").append(values.get(ctx.expr(0))).append("\n")
                .append(indent(values.get(ctx.block(0))));

        if (ctx.getText().contains("snsi")) {
            sb.append("\nelsif ").append(values.get(ctx.expr(1))).append("\n")
                    .append(indent(values.get(ctx.block(1))));
            if (ctx.block().size() == 3) {
                sb.append("\nelse\n").append(indent(values.get(ctx.block(2))));
            }
        } else if (ctx.block().size() == 2) {
            sb.append("\nelse\n").append(indent(values.get(ctx.block(1))));
        }

        sb.append("\nend\n");
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        if (ctx.getStart().getText().equals("tantque")) {
            String cond = values.get(ctx.expr());
            String body = values.get(ctx.block());
            result = "while " + cond + "\n" + indent(body) + "\nend\n";
        } else if (ctx.getStart().getText().equals("faire")) {
            String body = values.get(ctx.block());
            String cond = values.get(ctx.expr());
            result = "begin\n" + indent(body) + "\nend while " + cond + "\n";
        } else {
            String init = values.get(ctx.assignment(0));
            String cond = values.get(ctx.expr());
            String update = values.get(ctx.assignment(1));
            String body = values.get(ctx.block());
            result = init + indent("while " + cond + "\n" + indent(body + update) + "\nend\n");
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

    // Expressions follow Ruby syntax (very close to C), so these are mostly direct

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
    public void exitExpr(McdalangParser.ExprContext ctx) {
        if (ctx.getChildCount() == 1) {
            values.put(ctx, values.get(ctx.orExpr()));
        } else if (ctx.getChildCount() == 5 && ctx.getChild(1).getText().equals("?")) {
            String cond = values.get(ctx.orExpr());
            String thenExpr = values.get(ctx.expr(0));
            String elseExpr = values.get(ctx.expr(1));
            values.put(ctx, cond + " ? " + thenExpr + " : " + elseExpr);
        }
    }

    @Override
    public void exitOrExpr(McdalangParser.OrExprContext ctx) {
        if (ctx.andExpr().size() == 1) {
            values.put(ctx, values.get(ctx.andExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder(values.get(ctx.andExpr(0)));
            for (int i = 1; i < ctx.andExpr().size(); i++) {
                sb.append(" || ").append(values.get(ctx.andExpr(i)));
            }
            values.put(ctx, sb.toString());
        }
    }

    @Override
    public void exitAndExpr(McdalangParser.AndExprContext ctx) {
        if (ctx.notExpr().size() == 1) {
            values.put(ctx, values.get(ctx.notExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder(values.get(ctx.notExpr(0)));
            for (int i = 1; i < ctx.notExpr().size(); i++) {
                sb.append(" && ").append(values.get(ctx.notExpr(i)));
            }
            values.put(ctx, sb.toString());
        }
    }

    @Override
    public void exitNotExpr(McdalangParser.NotExprContext ctx) {
        if (ctx.getChildCount() == 2) {
            values.put(ctx, "!" + values.get(ctx.notExpr()));
        } else {
            values.put(ctx, values.get(ctx.concatenationExpr()));
        }
    }

    @Override
    public void exitAtom(McdalangParser.AtomContext ctx) {
        if (ctx.INT() != null) values.put(ctx, ctx.INT().getText());
        else if (ctx.FLOAT() != null) values.put(ctx, ctx.FLOAT().getText());
        else if (ctx.STRING() != null) values.put(ctx, ctx.STRING().getText());
        else if (ctx.CHAR() != null) values.put(ctx, "'" + ctx.CHAR().getText() + "'");
        else if (ctx.ID() != null) values.put(ctx, ctx.ID().getText());
        else if (ctx.funcCall() != null) values.put(ctx, values.get(ctx.funcCall()));
        else if (ctx.expr() != null) values.put(ctx, "(" + values.get(ctx.expr()) + ")");
        else if (ctx.getStart().getText().equals("true")) values.put(ctx, "true");
        else if (ctx.getStart().getText().equals("false")) values.put(ctx, "false");
    }

    @Override
    public String getCode() {
        return output.toString();
    }
}
