package n7.mcdalang.models.antlr;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class McdalangToC extends McdalangBaseListener {

    StringBuilder output = new StringBuilder();
    Stack<String> currentFunction = new Stack<>();

    @Override
    public void enterMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String returnType = mapType(ctx.type().getText());
        String name = ctx.ID().getText();
        currentFunction.push(name);

        output.append(returnType).append(" ").append(name).append("(");
        if (ctx.paramList() != null) {
            List<TerminalNode> params = ctx.paramList().ID();
            List<String> paramStrs = new ArrayList<>();
            for (TerminalNode param : params) {
                paramStrs.add("int " + param.getText()); // Par défaut en C
            }
            output.append(String.join(", ", paramStrs));
        }
        output.append(") {\n");
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        output.append("}\n\n");
        currentFunction.pop();
    }

    @Override
    public void enterVarDecl(McdalangParser.VarDeclContext ctx) {
        String type = mapType(ctx.type().getText());
        String name = ctx.ID().getText();
        output.append("  ").append(type).append(" ").append(name);
        if (ctx.expr() != null) {
            output.append(" = ").append(exprToString(ctx.expr()));
        }
        output.append(";\n");
    }

    @Override
    public void enterAssignment(McdalangParser.AssignmentContext ctx) {
        output.append("  ").append(ctx.ID().getText()).append(" = ")
                .append(exprToString(ctx.expr())).append(";\n");
    }

    @Override
    public void enterReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        output.append("  return ").append(exprToString(ctx.expr())).append(";\n");
    }

    @Override
    public void enterIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String varName = ctx.ID().getText();
        String op = ctx.getChild(1).getText(); // '++' ou '--'
        output.append("  ").append(varName).append(op).append(";\n");
    }

    @Override
    public void enterPrintStmt(McdalangParser.PrintStmtContext ctx) {
        output.append("  printf(\"%d\\n\", ").append(exprToString(ctx.expr())).append(");\n");
    }

    @Override
    public void enterFuncCall(McdalangParser.FuncCallContext ctx) {
        output.append("  ").append(exprToString(ctx)).append(";\n");
    }

    @Override
    public void enterIfStmt(McdalangParser.IfStmtContext ctx) {
        output.append("  if (").append(exprToString(ctx.expr(0))).append(") ");
        // Les blocs sont gérés dans enterBlock/exitBlock
    }

    @Override
    public void enterBlock(McdalangParser.BlockContext ctx) {
        ParseTree parent = ctx.getParent();
        if (!(parent instanceof McdalangParser.MethodDeclContext)) {
            output.append("{\n");
        }
    }
    @Override
    public void exitBlock(McdalangParser.BlockContext ctx) {
        ParseTree parent = ctx.getParent();
        if (!(parent instanceof McdalangParser.MethodDeclContext)) {
            output.append("}\n");
        }
    }


    private String exprToString(ParseTree tree) {
        if (tree == null) return "";

        if (tree instanceof TerminalNode) {
            return tree.getText();
        }

        if (tree instanceof McdalangParser.FuncCallContext) {
            McdalangParser.FuncCallContext ctx = (McdalangParser.FuncCallContext) tree;
            String name = ctx.ID().getText();
            List<String> args = new ArrayList<>();
            for (McdalangParser.ExprContext e : ctx.expr()) {
                args.add(exprToString(e));
            }
            return name + "(" + String.join(", ", args) + ")";
        }

        // Gérer x++, x-- en expression (postfixée)
        if (tree instanceof McdalangParser.PowExprContext) {
            ParseTree base = tree.getChild(0);
            if (tree.getChildCount() == 2) {
                String op = tree.getChild(1).getText();
                return exprToString(base) + op;
            }
            return exprToString(base);
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
            return exprToString(tree.getChild(1));
        }

        return tree.getText(); // fallback
    }

    // Mapper les types Mcdalang en types C
    private String mapType(String mcdType) {
        return switch (mcdType) {
            case "entier" -> "int";
            case "flottant" -> "float";
            case "chaine" -> "char*";
            case "bool" -> "bool";
            case "char" -> "char";
            case "vide" -> "void";
            default -> "int"; // fallback
        };
    }

    // Retourne le code généré
    public String getCCode() {
        return "#include <stdio.h>\n\n" + output.toString();
    }
}
