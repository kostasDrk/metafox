package ast;

/**
 * Abstract syntax tree visitor.
 * 
 */
public interface ASTVisitor {
	void visit(Program node) throws ASTVisitorException;
	void visit(AssignmentExpression node) throws ASTVisitorException;
	void visit(ExpressionStatement node) throws ASTVisitorException;
	void visit(IdentifierExpression node) throws ASTVisitorException;
	void visit(BinaryExpression node) throws ASTVisitorException;
	void visit(UnaryExpression node) throws ASTVisitorException;
	void visit(Member node) throws ASTVisitorException;
	void visit(ExtendedCall node) throws ASTVisitorException;
	void visit(LvalueCall node) throws ASTVisitorException;
	void visit(NormCall node) throws ASTVisitorException;
	void visit(MethodCall node) throws ASTVisitorException;
	void visit(Block node) throws ASTVisitorException;
	void visit(ArrayDef node) throws ASTVisitorException;
	void visit(FunctionDef node) throws ASTVisitorException;
	void visit(AnonymousFunctionCall node) throws ASTVisitorException;
	void visit(IntegerLiteral node) throws ASTVisitorException;
	void visit(DoubleLiteral node) throws ASTVisitorException;
	void visit(StringLiteral node) throws ASTVisitorException;
	void visit(NullLiteral node) throws ASTVisitorException;
	void visit(TrueLiteral node) throws ASTVisitorException;
	void visit(FalseLiteral node) throws ASTVisitorException;
}