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
                .map(line -> "    " + line)
                .collect(Collectors.joining("\n"));
    }

    private String translateType(String type) {
        // Ruby is dynamically typed, so we don't need type declarations
        return "";
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
            // Don't initialize to nil unless explicitly needed
            values.put(ctx, "");
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
            params = "";
            var ids = ctx.paramList().ID();
            params = String.join(", ", ids.stream().map(id -> id.getText()).collect(Collectors.toList()));
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
        values.put(ctx, String.join(".", callers) + "(" + String.join(", ", args) + ")");
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder sb = new StringBuilder();
        if (ctx.getText().contains("snsi")) {
            sb.append("if ").append(values.get(ctx.expr(0))).append("\n")
                    .append(indent(values.get(ctx.block(0)))).append("\nelsif ")
                    .append(values.get(ctx.expr(1))).append("\n")
                    .append(indent(values.get(ctx.block(1)))).append("\n");
            if (ctx.block().size() == 3) {
                sb.append("else\n").append(indent(values.get(ctx.block(2)))).append("\n");
            }
            sb.append("end\n");
        } else {
            sb.append("if ").append(values.get(ctx.expr(0))).append("\n")
                    .append(indent(values.get(ctx.block(0)))).append("\n");
            if (ctx.block().size() == 2) {
                sb.append("else\n").append(indent(values.get(ctx.block(1)))).append("\n");
            }
            sb.append("end\n");
        }
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        if (ctx.getStart().getText().equals("tantque")) {
            // While loop
            String cond = values.get(ctx.expr());
            String body = values.get(ctx.block());
            result = "while " + cond + "\n" + indent(body) + "\nend\n";
        } else if (ctx.getStart().getText().equals("faire")) {
            // Do-while loop
            String body = values.get(ctx.block());
            String cond = values.get(ctx.expr());
            result = "begin\n" + indent(body) + "\nend while " + cond + "\n";
        } else {
            // For loop
            String init = ctx.assignment().size() > 0 ? values.get(ctx.assignment(0)).replace("\n", "").trim() : "";
            String cond = ctx.expr() != null ? values.get(ctx.expr()).replace("\n", "").trim() : "";
            String update = ctx.assignment().size() > 1 ? values.get(ctx.assignment(1)).replace("\n", "").trim() : "";
            String body = values.get(ctx.block());

            // Extract loop components safely
            try {
                String var = init.split("=")[0].trim();
                String start = init.split("=")[1].trim();

                // Handle both numeric and variable end bounds
                String end = "";
                if (cond.contains("<=")) {
                    end = cond.split("<=")[1].trim();
                } else if (cond.contains("<")) {
                    end = cond.split("<")[1].trim();
                }


                String rangeOp = cond.contains("<=") ? ".." : "...";

                if (!end.isEmpty()) {
                    result = "for " + var + " in " + start + rangeOp + end + "\n" +
                            indent(body) + "\nend\n";
                } else {

                    result = init + "\nwhile " + cond + "\n" +
                            indent(body) + "\n" +
                            indent(update) + "\nend\n";
                }
            } catch (Exception e) {

                result = (!init.isEmpty() ? init + "\n" : "") +
                        "while " + cond + "\n" +
                        indent(body) + "\n" +
                        (!update.isEmpty() ? indent(update) + "\n" : "") +
                        "end\n";
            }
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
    public void exitConcatenationExpr(McdalangParser.ConcatenationExprContext ctx) {
        if (ctx.equalityExpr().size() == 1) {
            values.put(ctx, values.get(ctx.equalityExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ctx.equalityExpr().size(); i++) {
                String expr = values.get(ctx.equalityExpr(i));

                if (expr.matches("^\\d+$")) {
                    expr = expr + ".to_s";
                }
                if (i > 0) sb.append(" + ");
                sb.append(expr);
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
            // Ternary expression: condition ? then : else
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
                String op = ctx.getChild(2 * i - 1).getText(); // Get '||' or 'OR'
                if (op.equals("OR")) op = "||";
                sb.append(" ").append(op).append(" ").append(values.get(ctx.andExpr(i)));
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
                String op = ctx.getChild(2 * i - 1).getText(); // Get '&&' or 'AND'
                if (op.equals("AND")) op = "&&";
                sb.append(" ").append(op).append(" ").append(values.get(ctx.notExpr(i)));
            }
            values.put(ctx, sb.toString());
        }
    }

    @Override
    public void exitNotExpr(McdalangParser.NotExprContext ctx) {
        if (ctx.getChildCount() == 2) {
            String op = ctx.getChild(0).getText();
            if (op.equals("!") || op.equalsIgnoreCase("NOT")) {
                values.put(ctx, "!" + values.get(ctx.notExpr()));
                return;
            }
        }
        values.put(ctx, values.get(ctx.concatenationExpr()));
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
        else if (ctx.getStart().getText().equals("true")) values.put(ctx, "true");
        else if (ctx.getStart().getText().equals("false")) values.put(ctx, "false");
    }

    @Override
    public String getCode() {
        return output.toString();
    }
}