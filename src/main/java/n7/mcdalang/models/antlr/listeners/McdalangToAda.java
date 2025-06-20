package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;

public class McdalangToAda extends OutputBaseListener {
    private final ParseTreeProperty<String> values = new ParseTreeProperty<>();
    private final Map<String, String> symbolTable = new HashMap<>();
    private final StringBuilder output;

    public McdalangToAda() {
        output = new StringBuilder();
        output.append("with Ada.Text_IO; use Ada.Text_IO;\n\n");
    }

    private String indent(String code) {
        if (code == null || code.isBlank()) return "";
        return Arrays.stream(code.split("\n"))
                .filter(line -> !line.isBlank())
                .map(line -> "    " + line)
                .collect(Collectors.joining("\n"));
    }

    private String mapType(String mcdType) {
        return switch (mcdType) {
            case "entier" -> "Integer";
            case "flottant" -> "Float";
            case "chaine" -> "String";
            case "char" -> "Character";
            case "bool" -> "Boolean";
            case "vide" -> "";
            default -> "Integer";
        };
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
        String varName = ctx.ID().getText();
        String varType = mapType(ctx.type().getText());
        String varConst = ctx.getChild(0).getText().equals("const") ? "constant " : "";
        symbolTable.put(varName, varType);

        String exprStr = "";
        if (ctx.expr() != null) {
            exprStr = " := " + values.get(ctx.expr());
        }
        values.put(ctx, varName + " : " + varConst + varType + exprStr + ";" + "\n");
    }

    @Override
    public void exitAssignment(McdalangParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        String expr = values.get(ctx.expr());
        values.put(ctx, id + " := " + expr + ";" + "\n");
    }

    @Override
    public void exitIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String var = ctx.ID().getText();
        String op = ctx.getChild(1).getText();
        String adaOp = op.equals("++") ? " + 1" : " - 1";
        values.put(ctx, var + " := " + var + adaOp + ";" + "\n");
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "Put_Line(" + expr + ");" + "\n");
    }

    @Override
    public void exitReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : "";
        values.put(ctx, "return " + expr + ";" + "\n");
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String returnType = mapType(ctx.type().getText());
        String methodName = ctx.ID().getText();

        List<String> paramStrs = new ArrayList<>();
        if (ctx.paramList() != null) {
            for (int i = 0; i < ctx.paramList().type().size(); i++) {
                String paramType = mapType(ctx.paramList().type(i).getText());
                String paramName = ctx.paramList().ID(i).getText();
                symbolTable.put(paramName, paramType);
                paramStrs.add(paramName + " : in " + paramType);
            }
        }
        String params = String.join("; ", paramStrs);

        StringBuilder sb = new StringBuilder();
        if ("".equals(returnType)) {
            sb.append("procedure ").append(methodName).append("(").append(params).append(") is\n");
        } else {
            sb.append("function ").append(methodName).append("(").append(params).append(") return ").append(returnType).append(" is\n");
        }

        sb.append("begin").append("\n");
        sb.append(indent(values.get(ctx.block()))).append("\n");
        sb.append("end " + methodName + ";");

        values.put(ctx, sb.toString());
    }

    @Override
    public void exitFuncCall(McdalangParser.FuncCallContext ctx) {
        List<String> callers = new ArrayList<>();
        for (var id : ctx.ID()) callers.add(id.getText());
        List<String> args = new ArrayList<>();
        if (ctx.expr() != null) {
            for (var e : ctx.expr()) args.add(values.get(e));
        }
        String callStr = String.join(".", callers) + "(" + String.join(", ", args) + ")";
        values.put(ctx, callStr);
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder result = new StringBuilder();

        // Bloc "si"
        String cond = values.get(ctx.expr(0));
        String ifBody = indent(values.get(ctx.block(0)));
        result.append("if ").append(cond).append(" then\n").append(ifBody).append("\n");

        // Cas avec "sinonsi"
        for (int i = 1; i < ctx.expr().size(); i++) {
            String elifCond = values.get(ctx.expr(i));
            String elifBody = indent(values.get(ctx.block(i)));
            result.append("elsif ").append(elifCond).append(" then\n").append(elifBody).append("\n");
        }

        // Cas avec "sinon"
        if (ctx.block().size() > ctx.expr().size()) {
            String elseBody = indent(values.get(ctx.block(ctx.block().size() - 1)));
            result.append("else\n").append(elseBody).append("\n");
        }
        result.append("end if;");
        values.put(ctx, result.toString());
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        String keyword = ctx.getStart().getText();

        if (keyword.equals("tantque")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "while " + cond + " loop" + "\n" +
                    body + "\n" +
                    "end loop;";
        } else if (keyword.equals("faire")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            result = "loop" + "\n" +
                    body + "\n" +
                    indent("exit when not (" + cond + ");") + "\n" +
                    "end loop;";
        } else { // "pour"
            String init = values.get(ctx.assignment(0)).strip();
            String cond = values.get(ctx.expr()).strip();
            String update = values.get(ctx.assignment(1)).strip();

            String[] partsInit = init.split(":=");
            init = partsInit[0].strip() + " : Integer := " + partsInit[1].strip();

            String[] parts = init.split(":=");
            String var = parts[0].split(":")[0].strip();
            String start = parts[1].strip().replace(";", "");

            String end;
            boolean inclusive = cond.contains("<=");

            if (inclusive) {
                end = cond.replaceAll(".*<=", "").strip();
            } else {
                end = cond.replaceAll(".*<", "").strip();
            }

            String range = inclusive ? (start + " .. " + end) : (start + " .. (" + end + " - 1)");
            String blockRaw = values.get(ctx.block()).strip();
            String fullBlock = blockRaw + "\n" + update;
            String indentedBody = indent(fullBlock);

            result = "declare" + "\n" +
                    indent(init) + "\n" +
                    "begin" + "\n" +
                    indent("for " + var + " in " + range + " loop") + "\n" +
                    indent(indentedBody) + "\n" +
                    indent("end loop;") + "\n" +
                    "end;";
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
        values.put(ctx, values.get(ctx.orExpr()));
    }

    @Override
    public void exitOrExpr(McdalangParser.OrExprContext ctx) {
        if (ctx.andExpr().size() == 1) {
            values.put(ctx, values.get(ctx.andExpr(0)));
        } else {
            StringBuilder sb = new StringBuilder(values.get(ctx.andExpr(0)));
            for (int i = 1; i < ctx.andExpr().size(); i++) {
                sb.append(" or ").append(values.get(ctx.andExpr(i)));
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
                sb.append(" and ").append(values.get(ctx.notExpr(i)));
            }
            values.put(ctx, sb.toString());
        }
    }

    @Override
    public void exitNotExpr(McdalangParser.NotExprContext ctx) {
        if (ctx.getChildCount() == 2) { // '!' notExpr
            String expr = values.get(ctx.notExpr());
            values.put(ctx, "not " + expr);
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
                sb.append(" & ").append(values.get(ctx.equalityExpr(i)));  // concat Ada
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

    public String getCode() {
        return output.toString();
    }
}
