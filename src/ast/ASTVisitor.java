package ast;

/**
 * Abstract syntax tree visitor.
 * 
 */
public interface ASTVisitor {

	void visit(Program node) throws ASTVisitorException;
	void visit(AssignmentExpression node) throws ASTVisitorException;
	void visit(ExpressionStatement node) throws ASTVisitorException;
	void visit(Identifier node) throws ASTVisitorException;
	void visit(BinaryExpression node) throws ASTVisitorException;
	void visit(UnaryExpression node) throws ASTVisitorException;
	void visit(Lvalue node) throws ASTVisitorException;
}
