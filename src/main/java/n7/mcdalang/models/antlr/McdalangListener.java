// Generated from Mcdalang.g4 by ANTLR 4.13.2
package n7.mcdalang.models.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link McdalangParser}.
 */
public interface McdalangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link McdalangParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(McdalangParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(McdalangParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(McdalangParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(McdalangParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#incrStmt}.
	 * @param ctx the parse tree
	 */
	void enterIncrStmt(McdalangParser.IncrStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#incrStmt}.
	 * @param ctx the parse tree
	 */
	void exitIncrStmt(McdalangParser.IncrStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#methodDecl}.
	 * @param ctx the parse tree
	 */
	void enterMethodDecl(McdalangParser.MethodDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#methodDecl}.
	 * @param ctx the parse tree
	 */
	void exitMethodDecl(McdalangParser.MethodDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(McdalangParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(McdalangParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(McdalangParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(McdalangParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(McdalangParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(McdalangParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(McdalangParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(McdalangParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(McdalangParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(McdalangParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#printStmt}.
	 * @param ctx the parse tree
	 */
	void enterPrintStmt(McdalangParser.PrintStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#printStmt}.
	 * @param ctx the parse tree
	 */
	void exitPrintStmt(McdalangParser.PrintStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(McdalangParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(McdalangParser.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(McdalangParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(McdalangParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#loopStmt}.
	 * @param ctx the parse tree
	 */
	void enterLoopStmt(McdalangParser.LoopStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#loopStmt}.
	 * @param ctx the parse tree
	 */
	void exitLoopStmt(McdalangParser.LoopStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(McdalangParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(McdalangParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(McdalangParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(McdalangParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#concatenationExpr}.
	 * @param ctx the parse tree
	 */
	void enterConcatenationExpr(McdalangParser.ConcatenationExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#concatenationExpr}.
	 * @param ctx the parse tree
	 */
	void exitConcatenationExpr(McdalangParser.ConcatenationExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpr(McdalangParser.EqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpr(McdalangParser.EqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#relationalExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpr(McdalangParser.RelationalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#relationalExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpr(McdalangParser.RelationalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(McdalangParser.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(McdalangParser.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void enterMulExpr(McdalangParser.MulExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void exitMulExpr(McdalangParser.MulExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#powExpr}.
	 * @param ctx the parse tree
	 */
	void enterPowExpr(McdalangParser.PowExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#powExpr}.
	 * @param ctx the parse tree
	 */
	void exitPowExpr(McdalangParser.PowExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link McdalangParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(McdalangParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link McdalangParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(McdalangParser.AtomContext ctx);
}