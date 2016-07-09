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
}