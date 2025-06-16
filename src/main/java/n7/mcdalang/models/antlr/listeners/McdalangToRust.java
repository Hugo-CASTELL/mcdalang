package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;

import java.util.*;
import java.util.stream.Collectors;

public class McdalangToRust extends OutputBaseListener {
    public McdalangToRust() {
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
        String type = translateType(ctx.type().getText());
        String id = ctx.ID().getText();
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : defaultValue(type);
        values.put(ctx, "let mut " + id + ": " + type + " = " + expr + ";\n");
    }

    @Override
    public void exitAssignment(McdalangParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        String expr = values.get(ctx.expr());
        values.put(ctx, id + " = " + expr + ";\n");
    }

    @Override
    public void exitIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String var = ctx.ID().getText();
        String op = ctx.getChild(1).getText();
        String opRust = op.equals("++") ? "+= 1" : "-= 1";
        values.put(ctx, var + " " + opRust + ";\n");
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "println!(\"{}\", " + expr + ");\n");
    }

    @Override
    public void exitReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : "";
        values.put(ctx, expr.isBlank() ? "return;\n" : "return " + expr + ";\n");
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String returnType = translateType(ctx.type().getText());
        String name = ctx.ID().getText();
        String params = "";

        if (ctx.paramList() != null) {
            var ids = ctx.paramList().ID();
            var types = ctx.paramList().type();
            List<String> paramPairs = new ArrayList<>();
            for (int i = 0; i < ids.size(); i++) {
                paramPairs.add(ids.get(i).getText() + ": " + translateType(types.get(i).getText()));
            }
            params = String.join(", ", paramPairs);
        }

        String body = values.get(ctx.block());
        String sig = "fn " + name + "(" + params + ")";
        if (!returnType.equals("()")) sig += " -> " + returnType;

        values.put(ctx, sig + " {\n" + indent(body) + "\n}");
    }

    @Override
    public void exitFuncCall(McdalangParser.FuncCallContext ctx) {
        List<String> callers = new ArrayList<>();
        List<String> args = new ArrayList<>();
        for (var expr : ctx.expr()) args.add(values.get(expr));
        for (var caller : ctx.ID()) callers.add(caller.getText());
        values.put(ctx, String.join("::", callers) + "(" + String.join(", ", args) + ")");
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder result = new StringBuilder();

        // Bloc "si"
        String cond = values.get(ctx.expr(0));
        String ifBody = indent(values.get(ctx.block(0)));
        result.append("if ").append(cond).append(" {\n").append(ifBody).append("\n}");

        // Cas avec "sinonsi"
        for (int i = 1; i < ctx.expr().size(); i++) {
            String elifCond = values.get(ctx.expr(i));
            String elifBody = indent(values.get(ctx.block(i)));
            result.append(" else if ").append(elifCond).append(" {\n").append(elifBody).append("\n}");
        }

        // Cas avec "sinon"
        if (ctx.block().size() > ctx.expr().size()) {
            String elseBody = indent(values.get(ctx.block(ctx.block().size() - 1)));
            result.append(" else {\n").append(elseBody).append("\n}");
        }
        values.put(ctx, result.toString());
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        if (ctx.getStart().getText().equals("tantque")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "while " + cond + " {\n" + body + "\n}";
        } else if (ctx.getStart().getText().equals("faire")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "loop {\n" + body + "\n    if !(" + cond + ") { break; }\n}";
        } else { // pour
            String init = values.get(ctx.assignment(0)).strip();
            String cond = values.get(ctx.expr()).strip();
            String update = values.get(ctx.assignment(1)).strip();
            String[] parts = init.split("=");
            String var = parts[0].strip();
            String start = parts[1].strip().replace(";", "");

            String end = cond.replaceAll(".*<=", "").strip();
            boolean inclusive = cond.contains("<=");

            if (!inclusive) {
                end = cond.replaceAll(".*<", "").strip();
            }
            String range = inclusive ? (start + "..=" + end) : (start + ".." + end);
            String body = indent(values.get(ctx.block()));
            result = "for " + var + " in " + range + " {\n" + body + "\n}";
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
            values.put(ctx, "(" + left + ").pow(" + right + ")");
        }
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

    private String translateType(String mcdType) {
        return switch (mcdType) {
            case "entier" -> "i32";
            case "flottant" -> "f32";
            case "chaine" -> "String";
            case "bool" -> "bool";
            case "char" -> "char";
            case "vide" -> "()";
            case "tableau" -> "Vec<i32>";
            default -> "i32";
        };
    }

    private String defaultValue(String rustType) {
        return switch (rustType) {
            case "i32", "f32" -> "0";
            case "bool" -> "false";
            case "String" -> "String::new()";
            case "char" -> "'\\0'";
            default -> "Default::default()";
        };
    }

    @Override
    public String getCode() {
        return output.toString();
    }
}