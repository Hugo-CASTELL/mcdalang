package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;

public class McdalangToPowershell extends OutputBaseListener {
    ParseTreeProperty<String> values = new ParseTreeProperty<>();
    protected StringBuilder output;

    public McdalangToPowershell() {
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
        return switch (type) {
            case "entier", "flottant" -> "[int]";
            case "chaine", "char" -> "[string]";
            case "bool" -> "[bool]";
            case "vide" -> "void";
            default -> "[object]";
        };
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
        String id = "$" + ctx.ID().getText();
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : null;
        if (expr != null) {
            values.put(ctx, id + " = " + expr + "\n");
        } else {
            values.put(ctx, id + " = $null\n");
        }
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "Write-Output " + (expr != null ? expr : "") + "\n");
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        String startToken = ctx.getStart().getText();
        if (startToken.equals("tantque")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "while " + (cond != null ? cond : "$true") + " {\n" + body + "\n}\n";
        } else if (startToken.equals("faire")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "do {\n" + body + "\n} while " + (cond != null ? cond : "$true") + "\n";
        } else { // "pour"
            String init = values.get(ctx.assignment(0));
            String cond = values.get(ctx.expr());
            String update = values.get(ctx.assignment(1));
            init = init != null ? init.replace("\n", "").trim() : "";
            cond = cond != null ? cond : "$true";
            update = update != null ? update.replace("\n", "").trim() : "";
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
        if (ctx.getChildCount() > 0) {
            ParseTree child = ctx.getChild(0);
            if (child instanceof McdalangParser.ConcatenationExprContext) {
                values.put(ctx, values.get((McdalangParser.ConcatenationExprContext) child));
            } else {
                values.put(ctx, values.get(child));
            }
        } else {
            values.put(ctx, "");
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
                if (val != null) sb.append(" + ").append(val);
            }
            values.put(ctx, "(" + sb.toString() + ")");
        }
    }

    @Override
    public void exitEqualityExpr(McdalangParser.EqualityExprContext ctx) {
        if (ctx.relationalExpr().size() == 1) {
            values.put(ctx, values.get(ctx.relationalExpr(0)));
        } else {
            String left = values.get(ctx.relationalExpr(0));
            String right = values.get(ctx.relationalExpr(1));
            String op = ctx.getChild(1).getText().equals("==") ? "-eq" : "-ne";
            if (left != null && right != null) {
                values.put(ctx, "(" + left + " " + op + " " + right + ")");
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
            String op = switch (ctx.getChild(1).getText()) {
                case "<" -> "-lt";
                case ">" -> "-gt";
                case "<=" -> "-le";
                case ">=" -> "-ge";
                default -> ctx.getChild(1).getText();
            };
            if (left != null && right != null) {
                values.put(ctx, "(" + left + " " + op + " " + right + ")");
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
                values.put(ctx, "(" + left + " " + op + " " + right + ")");
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
                values.put(ctx, "(" + left + " " + op + " " + right + ")");
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
            String left = values.get(ctx.atom(0));
            String right = values.get(ctx.atom(1));
            if (left != null && right != null) {
                values.put(ctx, left + " -pow " + right);
            } else {
                values.put(ctx, left != null ? left : right != null ? right : "");
            }
        }
    }

    @Override
    public void exitAssignment(McdalangParser.AssignmentContext ctx) {
        String id = "$" + ctx.ID().getText();
        String expr = values.get(ctx.expr());
        if (expr != null) {
            values.put(ctx, id + " = " + expr + "\n");
        } else {
            values.put(ctx, id + " = $null\n");
        }
    }

    @Override
    public void exitIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String id = "$" + ctx.ID().getText();
        String op = ctx.getChild(1).getText().equals("++") ? " += 1" : " -= 1";
        values.put(ctx, id + op + "\n");
    }

    @Override
    public void exitFuncCall(McdalangParser.FuncCallContext ctx) {
        StringBuilder sb = new StringBuilder();
        String lastId = ctx.ID(ctx.ID().size() - 1).getText();
        if (lastId.equals("afficher")) {
            List<McdalangParser.ExprContext> args = ctx.expr();
            String argVal = args.isEmpty() ? "" : values.get(args.get(0));
            sb.append("Write-Output ").append(argVal != null ? argVal : "");
        } else {
            for (int i = 0; i < ctx.ID().size() - 1; i++) {
                sb.append(ctx.ID(i).getText()).append(".");
            }
            sb.append(lastId);
            List<McdalangParser.ExprContext> args = ctx.expr();
            if (!args.isEmpty()) {
                List<String> argVals = args.stream()
                        .map(values::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                sb.append("(").append(String.join(", ", argVals)).append(")");
            } else {
                sb.append("()");
            }
        }
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : "";
        if (expr.contains("+") || expr.contains("-") || expr.contains("*") || expr.contains("/")) {
            values.put(ctx, "return (" + expr + ")\n");
        } else {
            values.put(ctx, "return " + expr + "\n");
        }
    }

    @Override
    public void exitOrExpr(McdalangParser.OrExprContext ctx) {
        if (ctx.andExpr().size() == 1) {
            values.put(ctx, values.get(ctx.andExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder(values.get(ctx.andExpr(0)));
            for (int i = 1; i < ctx.andExpr().size(); i++) {
                String val = values.get(ctx.andExpr(i));
                sb.append(" -or ").append(val);
            }
            values.put(ctx, "(" + sb.toString() + ")");
        }
    }

    @Override
    public void exitAndExpr(McdalangParser.AndExprContext ctx) {
        if (ctx.notExpr().size() == 1) {
            values.put(ctx, values.get(ctx.notExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder(values.get(ctx.notExpr(0)));
            for (int i = 1; i < ctx.notExpr().size(); i++) {
                String val = values.get(ctx.notExpr(i));
                sb.append(" -and ").append(val);
            }
            values.put(ctx, "(" + sb.toString() + ")");
        }
    }

    @Override
    public void exitNotExpr(McdalangParser.NotExprContext ctx) {
        if (ctx.getChildCount() == 2 && ctx.getChild(0).getText().equals("!")) {
            String val = values.get(ctx.notExpr());
            values.put(ctx, "(-not " + val + ")");
        } else {
            values.put(ctx, values.get(ctx.concatenationExpr()));
        }
    }


    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String methodName = ctx.ID().getText();
        String params = "";
        if (ctx.paramList() != null) {
            List<String> paramList = new ArrayList<>();
            for (int i = 0; i < ctx.paramList().ID().size(); i++) {
                paramList.add("$" + ctx.paramList().ID(i).getText());
            }
            params = "(" + String.join(", ", paramList) + ")";
        }
        String body = values.get(ctx.block());
        String result = "function " + methodName + params + " {\n" + (body != null && !body.isEmpty() ? indent(body) + "\n" : "") + "}\n";
        values.put(ctx, result);
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder sb = new StringBuilder();
        String cond = values.get(ctx.expr(0));
        String block = values.get(ctx.block(0));
        sb.append("if ").append(cond != null ? cond : "$false").append(" {\n");
        sb.append(indent(block != null ? block : "")).append("\n}");
        int exprIdx = 1;
        int blockIdx = 1;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().equals("snsi")) {
                cond = values.get(ctx.expr(exprIdx++));
                block = values.get(ctx.block(blockIdx++));
                sb.append(" elseif ").append(cond != null ? cond : "$false").append(" {\n");
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
        else if (ctx.ID() != null) values.put(ctx, "$" + ctx.ID().getText());
        else if (ctx.funcCall() != null) values.put(ctx, values.get(ctx.funcCall()));
        else if (ctx.expr() != null) values.put(ctx, "(" + values.get(ctx.expr()) + ")");
        else if (ctx.getStart().getText().equals("true")) values.put(ctx, "$true");
        else if (ctx.getStart().getText().equals("false")) values.put(ctx, "$false");
        else values.put(ctx, "");
    }

    @Override
    public String getCode() {
        return output.toString();
    }
}