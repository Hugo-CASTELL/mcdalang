package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;


public class McdalangToCPlusPlus extends OutputBaseListener {
    private final Stack<String> currentFunction = new Stack<>();
    private final Map<String, String> symbolTable = new HashMap<>();
    private final Set<String> requiredHeaders = new HashSet<>();
    private int indentLevel = 0;
    private final StringBuilder output;

    public McdalangToCPlusPlus() {
        output = new StringBuilder();
        requiredHeaders.add("iostream");
        requiredHeaders.add("string");
    }

    @Override
    public void enterMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String returnType = mapType(ctx.type().getText());
        String name = ctx.ID().getText();
        currentFunction.push(name);

        output.append(indent()).append(returnType).append(" ").append(name).append("(");
        if (ctx.paramList() != null) {
            List<String> paramStrs = new ArrayList<>();
            for (int i = 0; i < ctx.paramList().type().size(); i++) {
                String paramType = mapType(ctx.paramList().type(i).getText());
                String paramName = ctx.paramList().ID(i).getText();
                symbolTable.put(paramName, paramType);
                paramStrs.add(paramType + " " + paramName);
            }
            output.append(String.join(", ", paramStrs));
        }
        output.append(") {\n");
        indentLevel++;
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        indentLevel--;
        output.append(indent()).append("}\n\n");
        currentFunction.pop();
    }

    @Override
    public void enterVarDecl(McdalangParser.VarDeclContext ctx) {
        String type = mapType(ctx.type().getText());
        String name = ctx.ID().getText();
        symbolTable.put(name, type);
        output.append(indent()).append(type).append(" ").append(name);
        if (ctx.expr() != null) {
            output.append(" = ").append(exprToString(ctx.expr()));
        }
        output.append(";\n");
    }

    @Override
    public void enterAssignment(McdalangParser.AssignmentContext ctx) {
        output.append(indent()).append(ctx.ID().getText()).append(" = ")
                .append(exprToString(ctx.expr())).append(";\n");
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
        String op = ctx.getChild(1).getText(); // '++' or '--'
        output.append(indent()).append(varName).append(op).append(";\n");
    }

    @Override
    public void enterPrintStmt(McdalangParser.PrintStmtContext ctx) {
        output.append(indent()).append("cout << ");
        output.append(exprToString(ctx.expr())).append(" << endl;\n");
    }

    @Override
    public void enterIfStmt(McdalangParser.IfStmtContext ctx) {
        output.append(indent()).append("if (").append(exprToString(ctx.expr(0))).append(") {\n");
        indentLevel++;
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        indentLevel--;
        output.append(indent()).append("}\n");
        // For simplicity: handle else-if and else outside or extend grammar and listener accordingly
    }

    @Override
    public void enterLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String firstToken = ctx.getChild(0).getText();
        if (firstToken.equals("tantque")) {
            output.append(indent()).append("while (")
                    .append(exprToString(ctx.expr())).append(") {\n");
            indentLevel++;
        } else if (firstToken.equals("faire")) {
            output.append(indent()).append("do {\n");
            indentLevel++;
        } else if (firstToken.equals("pour")) {
            // Assuming grammar: 'pour' '(' assignment ';' expr ';' assignment ')' block
            // For init:
            McdalangParser.AssignmentContext init = ctx.assignment(0);
            McdalangParser.ExprContext cond = ctx.expr();
            McdalangParser.AssignmentContext update = ctx.assignment(1);

            // For var type, you might need to parse from your symbol table or context
            // We'll infer type from symbolTable or fallback to int:
            String varName = init.ID().getText();
            String varType = symbolTable.getOrDefault(varName, "int");

            // Put in symbolTable if missing
            symbolTable.putIfAbsent(varName, varType);

            output.append(indent()).append("for (");
            output.append(varType).append(" ").append(varName).append(" = ")
                    .append(exprToString(init.expr())).append("; ");
            output.append(exprToString(cond)).append("; ");
            output.append(update.ID().getText()).append(" = ")
                    .append(exprToString(update.expr())).append(") {\n");
            indentLevel++;
        }
    }

    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        indentLevel--;
        String firstToken = ctx.getChild(0).getText();
        if (firstToken.equals("faire")) {
            output.append(indent()).append("} while (")
                    .append(exprToString(ctx.expr())).append(");\n");
        } else {
            output.append(indent()).append("}\n");
        }
    }

    private String exprToString(ParseTree tree) {
        if (tree == null) return "";

        if (tree instanceof TerminalNode) {
            return tree.getText();
        }

        if (tree instanceof McdalangParser.FuncCallContext ctx) {
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ctx.ID().size(); i++) {
                name.append(ctx.ID(i).getText());
                if (i < ctx.ID().size() - 1) name.append("::");
            }
            List<String> args = new ArrayList<>();
            for (McdalangParser.ExprContext e : ctx.expr()) {
                args.add(exprToString(e));
            }
            return name + "(" + String.join(", ", args) + ")";
        }

        if (tree.getChildCount() == 1) {
            return exprToString(tree.getChild(0));
        }

        if (tree.getChildCount() == 3) {
            String left = exprToString(tree.getChild(0));
            String op = tree.getChild(1).getText();
            String right = exprToString(tree.getChild(2));
            return left + " " + op + " " + right;
        }

        if (tree.getChildCount() == 2) {
            // Unary operator or postfix
            String op = tree.getChild(1).getText();
            return exprToString(tree.getChild(0)) + op;
        }

        return tree.getText();
    }

    private String mapType(String mcdType) {
        String type = switch (mcdType) {
            case "entier" -> "int";
            case "flottant" -> "float";
            case "chaine" -> "std::string";
            case "bool" -> "bool";
            case "char" -> "char";
            case "vide" -> "int";
            case "tableau" -> "std::vector<int>";
            default -> "int";
        };
        if (type.equals("std::string")) requiredHeaders.add("string");
        if (type.equals("std::vector<int>")) requiredHeaders.add("vector");
        return type;
    }

    private String indent() {
        return "  ".repeat(indentLevel);
    }

    @Override
    public String getCode() {
        StringBuilder headers = new StringBuilder();
        if (requiredHeaders.contains("iostream")) headers.append("#include <iostream>\n");
        if (requiredHeaders.contains("string")) headers.append("#include <string>\n");
        if (requiredHeaders.contains("vector")) headers.append("#include <vector>\n");
        headers.append("\nusing namespace std;\n\n");
        return headers + output.toString();
    }
}
