package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;

public class McdalangToJava extends OutputBaseListener {
    private final ParseTreeProperty<String> values = new ParseTreeProperty<>();
    private final boolean wrapMain;
    private String userMainBody = null;

    public McdalangToJava(boolean wrapMain) {
        this.output = new StringBuilder();
        this.wrapMain = wrapMain;
    }

    private String indent(String code) {
        if (code == null || code.isBlank()) return "";
        return Arrays.stream(code.split("\n"))
                .map(line -> "    " + line)
                .reduce((a, b) -> a + "\n" + b).orElse("");
    }

    private String translateType(String type) {
        return switch (type) {
            case "entier" -> "int";
            case "flottant" -> "float";
            case "chaine" -> "String";
            case "bool" -> "boolean";
            case "char" -> "char";
            case "vide" -> "void";
            default -> "int";
        };
    }

    @Override
    public void exitProg(McdalangParser.ProgContext ctx) {
        StringBuilder mainBody = new StringBuilder();
        StringBuilder methodCode = new StringBuilder();

        for (var stmtCtx : ctx.statement()) {
            ParseTree child = stmtCtx.getChild(0);
            if (child instanceof McdalangParser.MethodDeclContext) {
                if (!(values.get(child) == null || values.get(child).contains("main()"))) {
                    methodCode.append("\n").append(values.get(child)).append("\n");
                }
            } else {
                mainBody.append(values.get(stmtCtx));
            }
        }

        StringBuilder finalCode = new StringBuilder();

        if (wrapMain) {
            finalCode.append("public class Main {\n\n")
                    .append("    public static void main(String[] args) {\n");

            if (userMainBody != null) {
                finalCode.append(indent(indent(userMainBody.strip())));
            } else {
                finalCode.append(indent(indent(mainBody.toString().strip())));
            }

            finalCode.append("\n    }\n");

            finalCode.append(indent(methodCode.toString())).append("\n}");
        } else {
            finalCode.append(mainBody).append("\n").append(methodCode);
        }
        values.put(ctx, finalCode.toString());
        output.append(finalCode);
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
        if (ctx.expr() != null) {
            String expr = values.get(ctx.expr());
            values.put(ctx, type + " " + id + " = " + expr + ";\n");
        } else {
            values.put(ctx, type + " " + id + " = " + defaultValue(type) + ";\n");
        }
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
        values.put(ctx, var + op + ";\n");
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        values.put(ctx, "System.out.println(" + expr + ");\n");
    }

    @Override
    public void exitReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : "";
        values.put(ctx, "return " + expr + ";\n");
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String returnType = translateType(ctx.type().getText());
        String methodName = ctx.ID().getText();
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

        if (wrapMain && methodName.equals("main") && returnType.equals("void") && ctx.paramList() == null) {
            userMainBody = body;
            return;
        }

        values.put(ctx, "public static " + returnType + " " + methodName + "(" + params + ") {" + indent(body) + "\n}");
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder result = new StringBuilder();

        String cond = values.get(ctx.expr(0));
        String ifBody = indent(values.get(ctx.block(0)).strip());
        result.append("if (" + cond + ") {\n" + ifBody + "\n}");

        for (int i = 1; i < ctx.expr().size(); i++) {
            String elifCond = values.get(ctx.expr(i));
            String elifBody = indent(values.get(ctx.block(i)).strip());
            result.append(" else if (" + elifCond + ") {\n" + elifBody + "\n}");
        }

        if (ctx.block().size() > ctx.expr().size()) {
            String elseBody = indent(values.get(ctx.block(ctx.block().size() - 1)).strip());
            result.append(" else {\n" + elseBody + "\n}");
        }
        values.put(ctx, result.toString());
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        if (ctx.getStart().getText().equals("tantque")) {
            result = "while (" + values.get(ctx.expr()) + ") {\n" + indent(values.get(ctx.block()).strip()) + "\n}";
        } else if (ctx.getStart().getText().equals("faire")) {
            result = "do {\n" + indent(values.get(ctx.block()).strip()) + "\n} while (" + values.get(ctx.expr()) + ");";
        } else {
            String init = values.get(ctx.assignment(0)).replace(";\n", "");
            String cond = values.get(ctx.expr()).replace(";\n", "");
            String update = values.get(ctx.assignment(1)).replace(";\n", "");
            result = "for (" + init + "; " + cond + "; " + update + ") {\n" + indent(values.get(ctx.block()).strip()) + "\n}";
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
            String cond = values.get(ctx.orExpr());
            String thenExpr = values.get(ctx.expr(0));
            String elseExpr = values.get(ctx.expr(1));

            values.put(ctx, "(" + cond + ") ? (" + thenExpr + ") : (" + elseExpr + ")");
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
        if (ctx.getChildCount() == 2) { // '!' notExpr
            String expr = values.get(ctx.notExpr());
            values.put(ctx, "!" + expr);
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

    private String defaultValue(String javaType) {
        return switch (javaType) {
            case "int", "float" -> "0";
            case "boolean" -> "false";
            case "String" -> "\"\"";
            case "char" -> "'\\0'";
            default -> "null";
        };
    }

    @Override
    public String getCode() {
        return output.toString();
    }
}
