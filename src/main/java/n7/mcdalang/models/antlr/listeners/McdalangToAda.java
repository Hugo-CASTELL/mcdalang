package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;


public class McdalangToAda extends OutputBaseListener {
    private final Stack<String> currentFunction = new Stack<>();
    private final Map<String, String> symbolTable = new HashMap<>();
    private int indentLevel = 0;


    public McdalangToAda() {
        output = new StringBuilder();
        output.append("with Ada.Text_IO; use Ada.Text_IO;\n\n");
    }

    @Override
    public void enterMethodDecl(McdalangParser.MethodDeclContext ctx) {
        // Map Mcdalang return type to Ada type
        String returnType = mapType(ctx.type().getText());
        String name = ctx.ID().getText();
        currentFunction.push(name);

        // Declare procedure or function based on return type
        if ("vide".equals(ctx.type().getText())) {
            output.append("procedure ").append(name);
        } else {
            output.append("function ").append(name);
        }

        // Handle parameters
        output.append("(");
        if (ctx.paramList() != null) {
            List<String> paramStrs = new ArrayList<>();
            for (int i = 0; i < ctx.paramList().type().size(); i++) {
                String paramType = mapType(ctx.paramList().type(i).getText());
                String paramName = ctx.paramList().ID(i).getText();
                symbolTable.put(paramName, paramType);
                paramStrs.add(paramName + " : in " + paramType);
            }
            output.append(String.join("; ", paramStrs));
        }

        // Add return type for functions
        if (!"vide".equals(ctx.type().getText())) {
            output.append(") return ").append(returnType);
        } else {
            output.append(")");
        }

        // Add 'is' and 'begin' for Ada subprogram body
        output.append(" is\n");
        indentLevel++;
        output.append(indent()).append("begin\n");
        indentLevel++;
    }


    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        // Close subprogram with proper indentation
        indentLevel--;
        indentLevel--;
        output.append(indent()).append("end ").append(currentFunction.pop()).append(";\n\n");
    }

    @Override
    public void enterVarDecl(McdalangParser.VarDeclContext ctx) {
        String varName = ctx.ID().getText();
        String varType = mapType(ctx.type().getText());
        symbolTable.put(varName, varType);

        output.append(indent()).append(varName)
                .append(" : ").append(varType);
        if (ctx.expr() != null) {
            output.append(" := ").append(exprToString(ctx.expr()));
        }
        output.append(";\n");
    }

    @Override
    public void enterAssignment(McdalangParser.AssignmentContext ctx) {
        output.append(indent()).append(ctx.ID().getText())
                .append(" := ").append(exprToString(ctx.expr()))
                .append(";\n");
    }

    @Override
    public void enterReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        output.append(indent()).append("return ");
        if (ctx.expr() != null) {
            output.append(exprToString(ctx.expr()));
        }
        output.append(";\n");
    }

    @Override
    public void enterIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String varName = ctx.ID().getText();
        String op = ctx.getChild(1).getText();
        String adaOp = op.equals("++") ? " + 1" : " - 1";
        output.append(indent()).append(varName).append(" := ")
                .append(varName).append(adaOp).append(";\n");
    }

    @Override
    public void enterPrintStmt(McdalangParser.PrintStmtContext ctx) {
        output.append(indent()).append("Put_Line(");
        String exprType = determineExpressionType(ctx.expr());

        switch (exprType) {
            case "Integer" -> output.append("Integer'Image(");
            case "Float" -> output.append("Float'Image(");
            case "Boolean" -> output.append("Boolean'Image(");
        }

        output.append(exprToString(ctx.expr()));

        if (!"String".equals(exprType)) {
            output.append(")");
        }
        output.append(");\n");
    }

    @Override
    public void enterFuncCall(McdalangParser.FuncCallContext ctx) {
        output.append(indent());
        if (ctx.ID().size() > 1) {
            for (int i = 0; i < ctx.ID().size() - 1; i++) {
                output.append(ctx.ID(i).getText()).append(".");
            }
        }
        output.append(ctx.ID(ctx.ID().size() - 1).getText()).append("(");

        if (ctx.expr() != null && !ctx.expr().isEmpty()) {
            List<String> args = new ArrayList<>();
            for (McdalangParser.ExprContext e : ctx.expr()) {
                args.add(exprToString(e));
            }
            output.append(String.join(", ", args));
        }
        output.append(");\n");
    }

    @Override
    public void enterIfStmt(McdalangParser.IfStmtContext ctx) {
        output.append(indent()).append("if ")
                .append(exprToString(ctx.expr(0))).append(" then\n");
        indentLevel++;
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        indentLevel--;
        if (ctx.expr().size() > 1) {
            output.append(indent()).append("elsif ")
                    .append(exprToString(ctx.expr(1))).append(" then\n");
            indentLevel++;
        }
        if (ctx.block().size() > ctx.expr().size()) {
            output.append(indent()).append("else\n");
            indentLevel++;
        }
        output.append(indent()).append("end if;\n");
    }

    @Override
    public void enterLoopStmt(McdalangParser.LoopStmtContext ctx) {
        if (ctx.getChild(0).getText().equals("tantque")) {
            output.append(indent()).append("while ")
                    .append(exprToString(ctx.expr())).append(" loop\n");
        } else if (ctx.getChild(0).getText().equals("faire")) {
            output.append(indent()).append("loop\n");
        } else if (ctx.getChild(0).getText().equals("pour")) {
            output.append(indent()).append("declare\n");
            indentLevel++;
            output.append(indent()).append(exprToString(ctx.assignment(0))).append(";\n");
            indentLevel--;
            output.append(indent()).append("begin\n");
            indentLevel++;
            output.append(indent()).append("while ")
                    .append(exprToString(ctx.expr())).append(" loop\n");
        }
        indentLevel++;
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        indentLevel--;
        if (ctx.getChild(0).getText().equals("faire")) {
            output.append(indent()).append("exit when not (")
                    .append(exprToString(ctx.expr())).append(");\n");
        }
        output.append(indent()).append("end loop;\n");
        if (ctx.getChild(0).getText().equals("pour")) {
            output.append(indent()).append(exprToString(ctx.assignment(1))).append(";\n");
            indentLevel--;
            output.append(indent()).append("end;\n");
        }
    }

    private String determineExpressionType(ParseTree tree) {
        if (tree instanceof McdalangParser.ExprContext expr) {
            return determineExpressionType(expr.concatenationExpr());
        } else if (tree instanceof McdalangParser.ConcatenationExprContext) {
            // Handle concatenation expressions
            return "String";
        } else if (tree instanceof TerminalNode terminal) {
            String text = terminal.getText();
            if (text.matches("-?\\d+")) return "Integer";
            if (text.matches("-?\\d*\\.\\d+")) return "Float";
            if (text.equals("vrai") || text.equals("faux")) return "Boolean";
            if (text.startsWith("\"") && text.endsWith("\"")) return "String";
            // Check symbol table for variable type
            return symbolTable.getOrDefault(text, "Integer");
        }
        // Additional type determination for other expression rules...
        return "Integer";
    }

    private String exprToString(ParseTree tree) {
        if (tree == null) return "";
        if (tree instanceof TerminalNode) return tree.getText();
        return tree.getText(); // Simplified placeholder
    }

    private String mapType(String mcdType) {
        return switch (mcdType) {
            case "entier" -> "Integer";
            case "flottant" -> "Float";
            case "chaine" -> "String";
            case "bool" -> "Boolean";
            case "char" -> "Character";
            case "vide" -> "";
            default -> "Integer";
        };
    }

    private String indent() {
        return "  ".repeat(indentLevel);
    }

    public String getGeneratedCode() {
        return output.toString();
    }
}