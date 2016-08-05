package ast;

/**
 * Abstract syntax tree visitor.
 * 
 */
public interface ASTVisitor {
	Value visit(Program node) throws ASTVisitorException;
	Value visit(AssignmentExpression node) throws ASTVisitorException;
	Value visit(ExpressionStatement node) throws ASTVisitorException;
	Value visit(IdentifierExpression node) throws ASTVisitorException;
	Value visit(TermExpressionStmt node) throws ASTVisitorException;
	Value visit(BinaryExpression node) throws ASTVisitorException;
	Value visit(UnaryExpression node) throws ASTVisitorException;
	Value visit(Member node) throws ASTVisitorException;
	Value visit(ExtendedCall node) throws ASTVisitorException;
	Value visit(LvalueCall node) throws ASTVisitorException;
	Value visit(NormCall node) throws ASTVisitorException;
	Value visit(MethodCall node) throws ASTVisitorException;
	Value visit(Block node) throws ASTVisitorException;
	Value visit(ArrayDef node) throws ASTVisitorException;
	Value visit(FunctionDef node) throws ASTVisitorException;
	Value visit(FunctionDefExpression node) throws ASTVisitorException;
	Value visit(AnonymousFunctionCall node) throws ASTVisitorException;
	Value visit(IntegerLiteral node) throws ASTVisitorException;
	Value visit(DoubleLiteral node) throws ASTVisitorException;
	Value visit(StringLiteral node) throws ASTVisitorException;
	Value visit(NullLiteral node) throws ASTVisitorException;
	Value visit(TrueLiteral node) throws ASTVisitorException;
	Value visit(FalseLiteral node) throws ASTVisitorException;
	Value visit(IfStatement node) throws ASTVisitorException;
	Value visit(WhileStatement node) throws ASTVisitorException;
	Value visit(ForStatement node) throws ASTVisitorException;
	Value visit(BreakStatement node) throws ASTVisitorException;
	Value visit(ContinueStatement node) throws ASTVisitorException;
	Value visit(ReturnStatement node) throws ASTVisitorException;
	Value visit(ObjectDefinition node) throws ASTVisitorException;
	Value visit(IndexedElement node) throws ASTVisitorException;
}