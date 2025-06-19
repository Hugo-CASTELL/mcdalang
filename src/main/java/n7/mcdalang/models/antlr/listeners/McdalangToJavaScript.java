package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;

public class McdalangToJavaScript extends OutputBaseListener {
    ParseTreeProperty<String> values = new ParseTreeProperty<>();
    protected StringBuilder output;

    public McdalangToJavaScript() {
        output = new StringBuilder();
    }

    private String indent(String code) {
        if (code == null || code.isBlank()) return "";
        return Arrays.stream(code.split("\n"))
                .filter(line -> !line.isBlank())
                .map(line -> "    " + line)
                .collect(Collectors.joining("\n"));
    }

    private String translateType(String type) {
        return "let";  // you can adjust if needed
    }

    @Override
    public void exitProg(McdalangParser.ProgContext ctx) {
        StringBuilder sb = new StringBuilder();
        for (var stmt : ctx.statement()) {
            String val = values.get(stmt);
            if (val != null) sb.append(val);
        }
        values.put(ctx, sb.toString());
        output.append(sb.toString());
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
        values.put(ctx, "");
    }

    @Override
    public void exitVarDecl(McdalangParser.VarDeclContext ctx) {
        String id = ctx.ID().getText();
        boolean isConst = ctx.getText().startsWith("const");
        String declKeyword = isConst ? "const" : "let";
        String expr = ctx.expr() != null ? " = " + values.get(ctx.expr()) : "";
        values.put(ctx, declKeyword + " " + id + expr + ";\n");
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "console.log(" + (expr != null ? expr : "") + ");\n");
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        String startToken = ctx.getStart().getText();
        if (startToken.equals("tantque")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "while (" + (cond != null ? cond : "true") + ") {\n" + body + "\n}\n";
        } else if (startToken.equals("faire")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "do {\n" + body + "\n} while (" + (cond != null ? cond : "true") + ");\n";
        } else { // "pour"
            String init = values.get(ctx.assignment(0));
            String cond = values.get(ctx.expr());
            String update = values.get(ctx.assignment(1));
            init = init != null ? init.replace(";\n", "") : "";
            cond = cond != null ? cond : "true";
            update = update != null ? update.replace(";\n", "") : "";
            String body = indent(values.get(ctx.block()));
            result = "for (" + init + "; " + cond + "; " + update + ") {\n" + body + "\n}\n";
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
        if (ctx.getChildCount() == 5 && "?".equals(ctx.getChild(1).getText())) {
            // Ternary: orExpr '?' expr ':' expr
            String cond = values.get(ctx.orExpr());
            String trueExpr = values.get(ctx.expr(0));
            String falseExpr = values.get(ctx.expr(1));
            values.put(ctx, "(" + cond + " ? " + trueExpr + " : " + falseExpr + ")");
        } else {
            values.put(ctx, values.get(ctx.orExpr()));
        }
    }

    @Override
    public void exitOrExpr(McdalangParser.OrExprContext ctx) {
        if (ctx.andExpr().size() == 1) {
            values.put(ctx, values.get(ctx.andExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder(values.get(ctx.andExpr(0)));
            for (int i = 1; i < ctx.andExpr().size(); i++) {
                String op = ctx.getChild(2*i - 1).getText();
                String val = values.get(ctx.andExpr(i));
                if (val != null) {
                    sb.append(" ").append(op.equals("OR") ? "||" : op).append(" ").append(val);
                }
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
                String op = ctx.getChild(2*i - 1).getText();
                String val = values.get(ctx.notExpr(i));
                if (val != null) {
                    sb.append(" ").append(op.equals("AND") ? "&&" : op).append(" ").append(val);
                }
            }
            values.put(ctx, sb.toString());
        }
    }

    @Override
    public void exitNotExpr(McdalangParser.NotExprContext ctx) {
        if (ctx.getChildCount() == 2 && "!".equals(ctx.getChild(0).getText())) {
            String val = values.get(ctx.notExpr());
            values.put(ctx, "!" + val);
        } else {
            values.put(ctx, values.get(ctx.concatenationExpr()));
        }
    }

    @Override
    public void exitConcatenationExpr(McdalangParser.ConcatenationExprContext ctx) {
        if (ctx.equalityExpr().size() == 1) {
            values.put(ctx, values.get(ctx.equalityExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder(values.get(ctx.equalityExpr(0)));
            for (int i = 1; i < ctx.equalityExpr().size(); i++) {
                String val = values.get(ctx.equalityExpr(i));
                if (val != null) sb.append(" & ").append(val);
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
            if (left != null && right != null) {
                values.put(ctx, left + " " + op + " " + right);
            } else {
                values.put(ctx, left != null ? left : right != null ? right : "");
            }
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
            if (left != null && right != null) {
                values.put(ctx, left + " " + op + " " + right);
            } else {
                values.put(ctx, left != null ? left : right != null ? right : "");
            }
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
            if (left != null && right != null) {
                values.put(ctx, left + " " + op + " " + right);
            } else {
                values.put(ctx, left != null ? left : right != null ? right : "");
            }
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
            if (left != null && right != null) {
                if ("//".equals(op)) {
                    // Integer division in JS (Math.floor)
                    values.put(ctx, "Math.floor(" + left + " / " + right + ")");
                } else {
                    values.put(ctx, left + " " + op + " " + right);
                }
            } else {
                values.put(ctx, left != null ? left : right != null ? right : "");
            }
        }
    }

    @Override
    public void exitPowExpr(McdalangParser.PowExprContext ctx) {
        if (ctx.atom().size() == 1) {
            values.put(ctx, values.get(ctx.atom(0)));
        } else {
            // Support multiple '^' as nested Math.pow
            String base = values.get(ctx.atom(0));
            for (int i = 1; i < ctx.atom().size(); i++) {
                String exponent = values.get(ctx.atom(i));
                base = "Math.pow(" + base + ", " + exponent + ")";
            }
            values.put(ctx, base);
        }
    }

    @Override
    public void exitAssignment(McdalangParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        String expr = values.get(ctx.expr());
        if (expr != null) {
            values.put(ctx, id + " = " + expr + ";\n");
        } else {
            values.put(ctx, id + " = undefined;\n");
        }
    }

    @Override
    public void exitIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String id = ctx.ID().getText();
        String op = ctx.getChild(1).getText();
        String jsOp = op.equals("++") ? " += 1" : " -= 1";
        values.put(ctx, id + jsOp + ";\n");
    }

    @Override
    public void exitFuncCall(McdalangParser.FuncCallContext ctx) {
        StringBuilder sb = new StringBuilder();
        String lastId = ctx.ID(ctx.ID().size() - 1).getText();
        if (lastId.equals("afficher")) {
            List<McdalangParser.ExprContext> args = ctx.expr();
            String argVal = args.isEmpty() ? "" : values.get(args.get(0));
            sb.append("console.log(").append(argVal != null ? argVal : "").append(")");
            values.put(ctx, sb.toString() + ";\n");
        } else {
            for (int i = 0; i < ctx.ID().size() - 1; i++) {
                sb.append(ctx.ID(i).getText()).append(".");
            }
            sb.append(lastId).append("(");
            List<McdalangParser.ExprContext> args = ctx.expr();
            if (!args.isEmpty()) {
                List<String> argVals = args.stream()
                        .map(values::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                sb.append(String.join(", ", argVals));
            }
            sb.append(")");
            values.put(ctx, sb.toString());
        }
    }

    @Override
    public void exitReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : "";
        values.put(ctx, "return " + expr + ";\n");
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String methodName = ctx.ID().getText();
        String params = "";
        if (ctx.paramList() != null) {
            List<String> paramList = new ArrayList<>();
            for (int i = 0; i < ctx.paramList().ID().size(); i++) {
                paramList.add(ctx.paramList().ID(i).getText());
            }
            params = String.join(", ", paramList);
        }
        String body = values.get(ctx.block());
        String result = "function " + methodName + "(" + params + ") {\n" + indent(body != null ? body : "") + "\n}\n";
        values.put(ctx, result);
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder sb = new StringBuilder();
        String cond = values.get(ctx.expr(0));
        String block = values.get(ctx.block(0));
        sb.append("if (").append(cond != null ? cond : "false").append(") {\n");
        sb.append(indent(block != null ? block : "")).append("\n}");
        int exprIdx = 1;
        int blockIdx = 1;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if ("snsi".equals(ctx.getChild(i).getText())) {
                cond = values.get(ctx.expr(exprIdx++));
                block = values.get(ctx.block(blockIdx++));
                sb.append(" else if (").append(cond != null ? cond : "false").append(") {\n");
                sb.append(indent(block != null ? block : "")).append("\n}");
            }
        }
        if (ctx.block().size() > blockIdx) {
            block = values.get(ctx.block(blockIdx));
            sb.append(" else {\n").append(indent(block != null ? block : "")).append("\n}");
        }
        sb.append("\n");
        values.put(ctx, sb.toString());
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
        else values.put(ctx, "");
    }

    @Override
    public String getCode() {
        return output.toString();
    }
}