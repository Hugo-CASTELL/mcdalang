package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;

public class McdalangToCPlusPlus extends OutputBaseListener {
    private final ParseTreeProperty<String> values = new ParseTreeProperty<>();
    public McdalangToCPlusPlus() {
        output = new StringBuilder();
    }

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
        values.put(ctx, type + " " + id + " = " + expr + ";\n");
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
        String opCpp = op.equals("++") ? "++" : "--";
        values.put(ctx, var + opCpp + ";\n");
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "std::cout << " + expr + " << std::endl;\n");
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
                paramPairs.add(translateType(types.get(i).getText()) + " " + ids.get(i).getText());
            }
            params = String.join(", ", paramPairs);
        }

        String body = values.get(ctx.block());
        String sig = returnType + " " + name + "(" + params + ")";

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

        String cond = values.get(ctx.expr(0));
        String ifBody = indent(values.get(ctx.block(0)));
        result.append("if (" + cond + ") {\n" + ifBody + "\n}");

        for (int i = 1; i < ctx.expr().size(); i++) {
            String elifCond = values.get(ctx.expr(i));
            String elifBody = indent(values.get(ctx.block(i)));
            result.append(" else if (" + elifCond + ") {\n" + elifBody + "\n}");
        }

        if (ctx.block().size() > ctx.expr().size()) {
            String elseBody = indent(values.get(ctx.block(ctx.block().size() - 1)));
            result.append(" else {\n" + elseBody + "\n}");
        }
        values.put(ctx, result.toString());
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        if (ctx.getStart().getText().equals("tantque")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "while (" + cond + ") {\n" + body + "\n}";
        } else if (ctx.getStart().getText().equals("faire")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "do {\n" + body + "\n} while (" + cond + ");";
        } else {
            String init = values.get(ctx.assignment(0)).strip();
            String cond = values.get(ctx.expr()).strip();
            String update = values.get(ctx.assignment(1)).strip().replace(";","");
            String body = indent(values.get(ctx.block()));
            result = "for (" + init + " " + cond + "; " + update + ") {\n" + body + "\n}";
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
        if (ctx.getChildCount() == 1) {
            values.put(ctx, values.get(ctx.orExpr()));
        } else {
            String condition = values.get(ctx.orExpr());
            String trueExpr = values.get(ctx.expr(0));
            String falseExpr = values.get(ctx.expr(1));
            values.put(ctx, condition + " ? " + trueExpr + " : " + falseExpr);
        }
    }

    @Override
    public void exitOrExpr(McdalangParser.OrExprContext ctx) {
        StringBuilder result = new StringBuilder(values.get(ctx.andExpr(0)));
        for (int i = 1; i < ctx.andExpr().size(); i++) {
            result.append(" || ").append(values.get(ctx.andExpr(i)));
        }
        values.put(ctx, result.toString());
    }

    @Override
    public void exitAndExpr(McdalangParser.AndExprContext ctx) {
        StringBuilder result = new StringBuilder(values.get(ctx.notExpr(0)));
        for (int i = 1; i < ctx.notExpr().size(); i++) {
            result.append(" && ").append(values.get(ctx.notExpr(i)));
        }
        values.put(ctx, result.toString());
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
            values.put(ctx, "pow(" + left + ", " + right + ")");
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
            case "entier" -> "int";
            case "flottant" -> "float";
            case "chaine" -> "std::string";
            case "bool" -> "bool";
            case "char" -> "char";
            case "vide" -> "void";
            case "tableau" -> "std::vector<int>";
            default -> "int";
        };
    }

    private String defaultValue(String cppType) {
        return switch (cppType) {
            case "int", "float" -> "0";
            case "bool" -> "false";
            case "std::string" -> "\"\"";
            case "char" -> "'\\0'";
            default -> "{}";
        };
    }

    @Override
    public String getCode() {
        return """
        #include <iostream>
        #include <string>
        #include <vector>
        #include <cmath>

        using namespace std;

        """ + output.toString();
    }
}
