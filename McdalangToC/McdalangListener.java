// Generated from Mcdalang.g4 by ANTLR 4.0
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface McdalangListener extends ParseTreeListener {
	void enterLoopStmt(McdalangParser.LoopStmtContext ctx);
	void exitLoopStmt(McdalangParser.LoopStmtContext ctx);

	void enterConcatenationExpr(McdalangParser.ConcatenationExprContext ctx);
	void exitConcatenationExpr(McdalangParser.ConcatenationExprContext ctx);

	void enterAssignment(McdalangParser.AssignmentContext ctx);
	void exitAssignment(McdalangParser.AssignmentContext ctx);

	void enterMethodDecl(McdalangParser.MethodDeclContext ctx);
	void exitMethodDecl(McdalangParser.MethodDeclContext ctx);

	void enterReturnStmt(McdalangParser.ReturnStmtContext ctx);
	void exitReturnStmt(McdalangParser.ReturnStmtContext ctx);

	void enterType(McdalangParser.TypeContext ctx);
	void exitType(McdalangParser.TypeContext ctx);

	void enterRelationalExpr(McdalangParser.RelationalExprContext ctx);
	void exitRelationalExpr(McdalangParser.RelationalExprContext ctx);

	void enterProg(McdalangParser.ProgContext ctx);
	void exitProg(McdalangParser.ProgContext ctx);

	void enterPrintStmt(McdalangParser.PrintStmtContext ctx);
	void exitPrintStmt(McdalangParser.PrintStmtContext ctx);

	void enterIfStmt(McdalangParser.IfStmtContext ctx);
	void exitIfStmt(McdalangParser.IfStmtContext ctx);

	void enterAddExpr(McdalangParser.AddExprContext ctx);
	void exitAddExpr(McdalangParser.AddExprContext ctx);

	void enterStatement(McdalangParser.StatementContext ctx);
	void exitStatement(McdalangParser.StatementContext ctx);

	void enterParamList(McdalangParser.ParamListContext ctx);
	void exitParamList(McdalangParser.ParamListContext ctx);

	void enterMulExpr(McdalangParser.MulExprContext ctx);
	void exitMulExpr(McdalangParser.MulExprContext ctx);

	void enterIncrStmt(McdalangParser.IncrStmtContext ctx);
	void exitIncrStmt(McdalangParser.IncrStmtContext ctx);

	void enterBlock(McdalangParser.BlockContext ctx);
	void exitBlock(McdalangParser.BlockContext ctx);

	void enterExpr(McdalangParser.ExprContext ctx);
	void exitExpr(McdalangParser.ExprContext ctx);

	void enterFuncCall(McdalangParser.FuncCallContext ctx);
	void exitFuncCall(McdalangParser.FuncCallContext ctx);

	void enterPowExpr(McdalangParser.PowExprContext ctx);
	void exitPowExpr(McdalangParser.PowExprContext ctx);

	void enterAtom(McdalangParser.AtomContext ctx);
	void exitAtom(McdalangParser.AtomContext ctx);

	void enterVarDecl(McdalangParser.VarDeclContext ctx);
	void exitVarDecl(McdalangParser.VarDeclContext ctx);

	void enterEqualityExpr(McdalangParser.EqualityExprContext ctx);
	void exitEqualityExpr(McdalangParser.EqualityExprContext ctx);
}