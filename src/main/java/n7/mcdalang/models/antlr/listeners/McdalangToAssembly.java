package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangParser;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.stream.Collectors;

public class McdalangToAssembly extends OutputBaseListener {
    ParseTreeProperty<String> values = new ParseTreeProperty<>();
    protected StringBuilder output; // Assumed inherited from OutputBaseListener
    private int stackOffset = 0; // Track stack space for variables
    private int labelCount = 0; // For unique labels (e.g., if_else, end_if)
    private Map<String, Integer> varOffsets = new HashMap<>(); // Map variable names to stack offsets
    private boolean needsConsoleOutput = false; // Track if WriteConsoleA is needed

    public McdalangToAssembly() {
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
            case "entier" -> "DWORD"; // 4 bytes
            default -> "DWORD"; // Default to 4 bytes for simplicity
        };
    }

    @Override
    public void exitProg(McdalangParser.ProgContext ctx) {
        StringBuilder sb = new StringBuilder();
        // .DATA section
        sb.append("; x86-64 Assembly for Windows\n");
        sb.append("INCLUDELIB kernel32.lib\n");
        sb.append("INCLUDELIB ucrt.lib\n");
        sb.append("EXTRN GetStdHandle: PROC\n");
        if (needsConsoleOutput) {
            sb.append("EXTRN WriteConsoleA: PROC\n");
        }
        sb.append("EXTRN ExitProcess: PROC\n\n");
        sb.append(".DATA\n");
        sb.append("    buffer db 16 dup(0) ; Buffer for integer-to-string\n");
        if (needsConsoleOutput) {
            sb.append("    bytesWritten dq 0   ; Number of bytes written\n");
        }
        sb.append("\n.CODE\n");

        // Collect statements (e.g., main method)
        for (var stmt : ctx.statement()) {
            String val = values.get(stmt);
            if (val != null) sb.append(val);
        }

        // Append IntToString if needed
        if (needsConsoleOutput) {
            sb.append("\n; Convert integer in EAX to string in buffer, return length in RAX\n");
            sb.append("IntToString PROC\n");
            sb.append("    push rbx\n");
            sb.append("    push rsi\n");
            sb.append("    mov rsi, rcx        ; Buffer address\n");
            sb.append("    mov ebx, eax        ; Save number\n");
            sb.append("    mov ecx, 10         ; Divisor\n");
            sb.append("    xor r8, r8          ; Digit count\n");
            sb.append("convert_loop:\n");
            sb.append("    xor edx, edx        ; Clear EDX for division\n");
            sb.append("    div ecx             ; Divide EAX by 10\n");
            sb.append("    add dl, '0'         ; Convert remainder to ASCII\n");
            sb.append("    push rdx            ; Save digit\n");
            sb.append("    inc r8              ; Increment digit count\n");
            sb.append("    test eax, eax       ; Check if quotient is 0\n");
            sb.append("    jnz convert_loop\n");
            sb.append("    mov rcx, r8         ; Save length\n");
            sb.append("    xor r9, r9          ; Index\n");
            sb.append("write_loop:\n");
            sb.append("    pop rax             ; Get digit\n");
            sb.append("    mov [rsi+r9], al    ; Write to buffer\n");
            sb.append("    inc r9\n");
            sb.append("    cmp r9, rcx\n");
            sb.append("    jl write_loop\n");
            sb.append("    mov byte ptr [rsi+r9], 0 ; Null-terminate\n");
            sb.append("    mov rax, rcx        ; Return length\n");
            sb.append("    pop rsi\n");
            sb.append("    pop rbx\n");
            sb.append("    ret\n");
            sb.append("IntToString ENDP\n");
        }

        sb.append("END\n");
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
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : null;
        // Allocate stack space
        stackOffset += 4; // 4 bytes for DWORD
        varOffsets.put(id, stackOffset);
        StringBuilder sb = new StringBuilder();
        if (expr != null && expr.matches("\\d+")) {
            // Initialize with constant
            sb.append("    mov dword ptr [rbp-").append(stackOffset).append("], ").append(expr).append("\n");
        } else if (expr != null) {
            // Assume expr is in EAX (simplified)
            sb.append("    mov dword ptr [rbp-").append(stackOffset).append("], eax\n");
        }
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitPrintStmt(McdalangParser.PrintStmtContext ctx) {
        String expr = values.get(ctx.expr());
        needsConsoleOutput = true;
        StringBuilder sb = new StringBuilder();
        // Assume expr is a variable or constant
        if (expr != null && expr.matches("\\d+")) {
            sb.append("    mov eax, ").append(expr).append("\n");
        } else if (expr != null && varOffsets.containsKey(expr)) {
            int offset = varOffsets.get(expr);
            sb.append("    mov eax, [rbp-").append(offset).append("]\n");
        }
        sb.append("    lea rcx, buffer\n");
        sb.append("    call IntToString\n");
        sb.append("    mov rbx, rax\n");
        sb.append("    mov rcx, -11\n");
        sb.append("    call GetStdHandle\n");
        sb.append("    mov rcx, rax\n");
        sb.append("    lea rdx, buffer\n");
        sb.append("    mov r8, rbx\n");
        sb.append("    lea r9, bytesWritten\n");
        sb.append("    mov qword ptr [rsp+32], 0\n");
        sb.append("    call WriteConsoleA\n");
        values.put(ctx, sb.toString());
    }


    @Override
    public void exitLoopStmt(McdalangParser.LoopStmtContext ctx) {
        String result;
        String startToken = ctx.getStart().getText();
        StringBuilder sb = new StringBuilder();
        String loopLabel = "loop_" + (labelCount++);
        String endLabel = "end_loop_" + labelCount;
        if (startToken.equals("tantque")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            sb.append(loopLabel).append(":\n");
            if (cond != null && cond.contains("cmp")) {
                sb.append(cond); // Assume cond is comparison code
            } else {
                sb.append("    mov eax, 1\n"); // Default true
            }
            sb.append("    test eax, eax\n");
            sb.append("    jz ").append(endLabel).append("\n");
            sb.append(body);
            sb.append("    jmp ").append(loopLabel).append("\n");
            sb.append(endLabel).append(":\n");
        } else if (startToken.equals("faire")) {
            String cond = values.get(ctx.expr());
            String body = indent(values.get(ctx.block()));
            sb.append(loopLabel).append(":\n");
            sb.append(body);
            if (cond != null && cond.contains("cmp")) {
                sb.append(cond);
            } else {
                sb.append("    mov eax, 1\n");
            }
            sb.append("    test eax, eax\n");
            sb.append("    jnz ").append(loopLabel).append("\n");
        } else { // "pour"
            String init = values.get(ctx.assignment(0));
            String cond = values.get(ctx.expr());
            String update = values.get(ctx.assignment(1));
            sb.append(init != null ? init : "");
            sb.append(loopLabel).append(":\n");
            if (cond != null && cond.contains("cmp")) {
                sb.append(cond);
            } else {
                sb.append("    mov eax, 1\n");
            }
            sb.append("    test eax, eax\n");
            sb.append("    jz ").append(endLabel).append("\n");
            sb.append(indent(values.get(ctx.block())));
            sb.append(update != null ? update : "");
            sb.append("    jmp ").append(loopLabel).append("\n");
            sb.append(endLabel).append(":\n");
        }
        values.put(ctx, sb.toString());
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
            StringBuilder sb = new StringBuilder();
            String left = values.get(ctx.equalityExpr(0));
            sb.append(left); // Simplified: assume left is a variable or constant
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
            StringBuilder sb = new StringBuilder();
            if (varOffsets.containsKey(left)) {
                sb.append("    mov eax, [rbp-").append(varOffsets.get(left)).append("]\n");
            } else if (left.matches("\\d+")) {
                sb.append("    mov eax, ").append(left).append("\n");
            }
            if (varOffsets.containsKey(right)) {
                sb.append("    cmp eax, [rbp-").append(varOffsets.get(right)).append("]\n");
            } else if (right.matches("\\d+")) {
                sb.append("    cmp eax, ").append(right).append("\n");
            }
            sb.append("    set").append(op.equals("==") ? "e" : "ne").append(" al\n");
            sb.append("    movzx eax, al\n");
            values.put(ctx, sb.toString());
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
            StringBuilder sb = new StringBuilder();
            if (varOffsets.containsKey(left)) {
                sb.append("    mov eax, [rbp-").append(varOffsets.get(left)).append("]\n");
            } else if (left.matches("\\d+")) {
                sb.append("    mov eax, ").append(left).append("\n");
            }
            if (varOffsets.containsKey(right)) {
                sb.append("    cmp eax, [rbp-").append(varOffsets.get(right)).append("]\n");
            } else if (right.matches("\\d+")) {
                sb.append("    cmp eax, ").append(right).append("\n");
            }
            String setInst = switch (op) {
                case "<" -> "l";
                case ">" -> "g";
                case "<=" -> "le";
                case ">=" -> "ge";
                default -> "e";
            };
            sb.append("    set").append(setInst).append(" al\n");
            sb.append("    movzx eax, al\n");
            values.put(ctx, sb.toString());
        }
    }

    @Override
    public void exitAddExpr(McdalangParser.AddExprContext ctx) {
        if (ctx.mulExpr().size() == 1) {
            values.put(ctx, values.get(ctx.mulExpr(0)));
        } else {
            String left = values.get(ctx.mulExpr(0));
            String right = values.get(ctx.mulExpr(1));
            values.put(ctx, left); // Simplified: return left operand
        }
    }

    @Override
    public void exitMulExpr(McdalangParser.MulExprContext ctx) {
        if (ctx.powExpr().size() == 1) {
            values.put(ctx, values.get(ctx.powExpr(0)));
        } else {
            String left = values.get(ctx.powExpr(0));
            String right = values.get(ctx.powExpr(1));
            values.put(ctx, left);
        }
    }

    @Override
    public void exitPowExpr(McdalangParser.PowExprContext ctx) {
        if (ctx.atom().size() == 1) {
            values.put(ctx, values.get(ctx.atom(0)));
        } else {
            values.put(ctx, values.get(ctx.atom(0))); // Power not supported
        }
    }

    @Override
    public void exitAssignment(McdalangParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        String expr = values.get(ctx.expr());
        StringBuilder sb = new StringBuilder();
        if (varOffsets.containsKey(id) && expr != null) {
            if (expr.matches("\\d+")) {
                sb.append("    mov dword ptr [rbp-").append(varOffsets.get(id)).append("], ").append(expr).append("\n");
            } else if (varOffsets.containsKey(expr)) {
                sb.append("    mov eax, [rbp-").append(varOffsets.get(expr)).append("]\n");
                sb.append("    mov [rbp-").append(varOffsets.get(id)).append("], eax\n");
            }
        }
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitIncrStmt(McdalangParser.IncrStmtContext ctx) {
        String id = ctx.ID().getText();
        String op = ctx.getChild(1).getText().equals("++") ? "inc" : "dec";
        StringBuilder sb = new StringBuilder();
        if (varOffsets.containsKey(id)) {
            sb.append("    ").append(op).append(" dword ptr [rbp-").append(varOffsets.get(id)).append("]\n");
        }
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitFuncCall(McdalangParser.FuncCallContext ctx) {
        StringBuilder sb = new StringBuilder();
        String lastId = ctx.ID(ctx.ID().size() - 1).getText();
        if (lastId.equals("afficher")) {
            needsConsoleOutput = true;
            List<McdalangParser.ExprContext> args = ctx.expr();
            String argVal = args.isEmpty() ? null : values.get(args.get(0));
            if (argVal != null) {
                if (argVal.matches("\\d+")) {
                    sb.append("    mov eax, ").append(argVal).append("\n");
                } else if (varOffsets.containsKey(argVal)) {
                    sb.append("    mov eax, [rbp-").append(varOffsets.get(argVal)).append("]\n");
                }
                sb.append("    lea rcx, buffer\n");
                sb.append("    call IntToString\n");
                sb.append("    mov rbx, rax\n");
                sb.append("    mov rcx, -11\n");
                sb.append("    call GetStdHandle\n");
                sb.append("    mov rcx, rax\n");
                sb.append("    lea rdx, buffer\n");
                sb.append("    mov r8, rbx\n");
                sb.append("    lea r9, bytesWritten\n");
                sb.append("    mov qword ptr [rsp+32], 0\n");
                sb.append("    call WriteConsoleA\n");
            }
        } else {
            // Ignore other function calls for simplicity
            sb.append("; Call to ").append(lastId).append(" ignored\n");
        }
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitReturnStmt(McdalangParser.ReturnStmtContext ctx) {
        String expr = ctx.expr() != null ? values.get(ctx.expr()) : null;
        StringBuilder sb = new StringBuilder();
        if (expr != null && expr.matches("\\d+")) {
            sb.append("    mov eax, ").append(expr).append("\n");
        }
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitMethodDecl(McdalangParser.MethodDeclContext ctx) {
        String methodName = ctx.ID().getText();
        StringBuilder sb = new StringBuilder();
        stackOffset = 0; // Reset stack offset for new procedure
        varOffsets.clear(); // Clear variable offsets
        sb.append(methodName).append(" PROC\n");
        // Prologue
        sb.append("    push rbp\n");
        sb.append("    mov rbp, rsp\n");
        sb.append("    sub rsp, 32         ; Shadow space + alignment\n");
        // Body
        String body = values.get(ctx.block());
        if (body != null) sb.append(indent(body));
        // Epilogue
        sb.append("    mov rsp, rbp\n");
        sb.append("    pop rbp\n");
        if (methodName.equals("main")) {
            sb.append("    xor ecx, ecx\n");
            sb.append("    call ExitProcess\n");
        } else {
            sb.append("    ret\n");
        }
        sb.append(methodName).append(" ENDP\n\n");
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitIfStmt(McdalangParser.IfStmtContext ctx) {
        StringBuilder sb = new StringBuilder();
        String elseLabel = "else_" + (labelCount++);
        String endLabel = "end_if_" + labelCount;
        String cond = values.get(ctx.expr(0));
        String block = values.get(ctx.block(0));
        if (cond != null && cond.contains("cmp")) {
            sb.append(cond);
        }
        sb.append("    jz ").append(elseLabel).append("\n");
        if (block != null) sb.append(indent(block));
        sb.append("    jmp ").append(endLabel).append("\n");
        sb.append(elseLabel).append(":\n");
        int exprIdx = 1;
        int blockIdx = 1;
        while (ctx.getChildCount() > 2 * blockIdx + 1 && ctx.getChild(2 * blockIdx + 1).getText().equals("snsi")) {
            String elseIfLabel = "else_if_" + (labelCount++);
            cond = values.get(ctx.expr(exprIdx++));
            block = values.get(ctx.block(blockIdx++));
            if (cond != null && cond.contains("cmp")) {
                sb.append(cond);
            }
            sb.append("    jz ").append(elseIfLabel).append("\n");
            if (block != null) sb.append(indent(block));
            sb.append("    jmp ").append(endLabel).append("\n");
            sb.append(elseIfLabel).append(":\n");
        }
        if (ctx.block().size() > blockIdx) {
            block = values.get(ctx.block(blockIdx));
            if (block != null) sb.append(indent(block));
        }
        sb.append(endLabel).append(":\n");
        values.put(ctx, sb.toString());
    }

    @Override
    public void exitAtom(McdalangParser.AtomContext ctx) {
        if (ctx.INT() != null) values.put(ctx, ctx.INT().getText());
        else if (ctx.ID() != null) values.put(ctx, ctx.ID().getText());
        else if (ctx.funcCall() != null) values.put(ctx, values.get(ctx.funcCall()));
        else if (ctx.expr() != null) values.put(ctx, values.get(ctx.expr()));
        else values.put(ctx, "");
    }

    @Override
    public String getCode() {
        return output.toString();
    }
}