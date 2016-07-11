import ast.ASTVisitor;
import ast.ASTVisitorException;
import ast.Program;
import ast.Statement;
import ast.IfStatement;
import ast.WhileStatement;
import ast.ForStatement;
import ast.ReturnStatement;
import ast.Expression;
import ast.ExpressionStatement;
import ast.AssignmentExpression;
import ast.BinaryExpression;
import ast.UnaryExpression;
import ast.TermExpression;
import ast.TermExpressionStmt;
import ast.IdentifierExpression;
import ast.Operator;
import ast.Primary;
import ast.Lvalue;
import ast.Member;
import ast.Constant;
import ast.IntegerLiteral;
import ast.DoubleLiteral;
import ast.StringLiteral;
import ast.TrueLiteral;
import ast.FalseLiteral;
import ast.NullLiteral;
import ast.Call;
import ast.ExtendedCall;
import ast.LvalueCall;
import ast.AnonymousFunctionCall;
import ast.CallSuffix;
import ast.NormCall;
import ast.MethodCall;
import ast.Block;
import ast.ArrayDef;
import ast.FunctionDef;
import ast.FunctionDefExpression;
import ast.ObjectDefinition;
import ast.IndexedElement;

public class PrintASTVisitor implements ASTVisitor{
	private int margin = 0; //how much white space ('\t') preceding in each line
    
    private void format() { //add white space to have a nice format
        for(int i = 0; i < margin; i++)
            System.out.print("\t");
    }
    
    private void addmargin() { //adds more white space to the end of a printed line
        margin++;
    }
    
    private void submargin() { //subtracts white space
        margin--;
    }

    private void semicolon(){
    	System.out.println(";");
    }

    @Override
    public void visit(Program node) throws ASTVisitorException {
    	for(Statement stmt : node.getStatements())
    		stmt.accept(this);
    }

    @Override
    public void visit(ExpressionStatement node) throws ASTVisitorException {
    	node.getExpression().accept(this);
    	semicolon();
    }

    @Override
    public void visit(AssignmentExpression node) throws ASTVisitorException {
    	node.getLvalue().accept(this);
    	System.out.print(" = ");
    	node.getExpression().accept(this);
    }

    @Override
    public void visit(BinaryExpression node) throws ASTVisitorException {
    	node.getExpression1().accept(this);
    	System.out.print(" "+node.getOperator()+" ");
    	node.getExpression2().accept(this);
    }

    @Override
    public void visit(TermExpressionStmt node) throws ASTVisitorException {
    	System.out.print("(");
    	node.getExpression().accept(this);
    	System.out.print(")");
    }

    @Override
    public void visit(UnaryExpression node) throws ASTVisitorException {
    	System.out.print(node.getOperator());
    	if(node.getExpression() != null)
    		node.getExpression().accept(this);
    	else
    		node.getLvalue().accept(this);
    }

    @Override
    public void visit(IdentifierExpression node) throws ASTVisitorException {
    	if(!node.isLocal())
    		System.out.print("::");
    	System.out.print(node.getIdentifier());
    }

    @Override
    public void visit(Member node) throws ASTVisitorException {
    	if(node.getLvalue() != null){
    		node.getLvalue().accept(this);
    	}else if(node.getCall() != null){
    		node.getCall().accept(this);
    	}
    	if(node.getIdentifier() != null)
    		System.out.print("."+node.getIdentifier());
    	else if(node.getExpression() != null){
    		System.out.print("[");
    		node.getExpression().accept(this);
    		System.out.print("]");
    	}
    }

    @Override
    public void visit(ExtendedCall node) throws ASTVisitorException {
    	node.getCall().accept(this);
    	System.out.print("(");
    	node.getExpressionList();
    	for(Expression expression : node.getExpressionList()){
    		expression.accept(this);
    		System.out.print(",");
    	}
    	System.out.print(")");
    }

    @Override
    public void visit(LvalueCall node) throws ASTVisitorException {
    	node.getLvalue().accept(this);
    	node.getCallSuffix().accept(this);
    }

    @Override
    public void visit(AnonymousFunctionCall node) throws ASTVisitorException {
    	System.out.print("(");
    	node.getFunctionDef().accept(this);
    	System.out.print(")");

    	System.out.print("(");
    	for(Expression expression : node.getExpressionList()){
    		expression.accept(this);
    		System.out.print(",");
    	}
    	System.out.print(")");
    }

    @Override
    public void visit(NormCall node) throws ASTVisitorException {
    	System.out.print("(");
    	for(Expression expression : node.getExpressionList()){
    		expression.accept(this);
    		System.out.print(",");
    	}
    	System.out.print(")");
    }

    @Override
    public void visit(MethodCall node) throws ASTVisitorException {
    	System.out.print(".."+node.getIdentifier()+"(");
    	for(Expression expression : node.getExpressionList()){
    		expression.accept(this);
    		System.out.print(",");
    	}
    	System.out.print(")");
    }

    @Override
    public void visit(ObjectDefinition node) throws ASTVisitorException {
    	System.out.print("{");
    	for(IndexedElement indexed : node.getIndexedElementList()){
    		indexed.accept(this);
    		System.out.print(",\n\t");
    	}
    	System.out.print("}");
    }

    @Override
    public void visit(IndexedElement node) throws ASTVisitorException {
    	node.getExpression1().accept(this);
    	System.out.print(" : ");
    	node.getExpression2().accept(this);
    }

    @Override
    public void visit(ArrayDef node) throws ASTVisitorException {
    	System.out.print("[");
    	for(Expression expression : node.getExpressionList()){
    		expression.accept(this);
    		System.out.print(",");
    	}
    	System.out.print("]");
    }

    @Override
    public void visit(Block node) throws ASTVisitorException {
    	System.out.println("{");
    	for(Statement stmt : node.getStatementList()){
    		stmt.accept(this);
    	}
    	System.out.println("}");
    }

    @Override
    public void visit(FunctionDefExpression node) throws ASTVisitorException {
    	node.getFunctionDef().accept(this);
    }

    @Override
    public void visit(FunctionDef node) throws ASTVisitorException {
    	System.out.print("function ");
    	System.out.print(node.getFuncName());
    	System.out.print("(");
    	for(String id : node.getArguments()){
    		System.out.print(id+", ");
    	}
    	System.out.print(")");
    	node.getBody().accept(this);
    }

    @Override
    public void visit(IntegerLiteral node) throws ASTVisitorException {
    	System.out.print(node.getLiteral());
    }

    @Override
    public void visit(DoubleLiteral node) throws ASTVisitorException {
    	System.out.print(node.getLiteral());
    }

    @Override
    public void visit(StringLiteral node) throws ASTVisitorException {
    	System.out.print("\""+node.getLiteral()+"\"");
    }

    @Override
    public void visit(NullLiteral node) throws ASTVisitorException {
    	System.out.print("nil");
    }

    @Override
    public void visit(TrueLiteral node) throws ASTVisitorException {
    	System.out.print("true");
    }

    @Override
    public void visit(FalseLiteral node) throws ASTVisitorException {
    	System.out.print("false");
    }

    @Override
    public void visit(IfStatement node) throws ASTVisitorException {
    	System.out.print("if(");
    	node.getExpression().accept(this);
    	System.out.println(")");
    	node.getStatement().accept(this);
    	node.getElseStatement().accept(this);
    }

    @Override
    public void visit(WhileStatement node) throws ASTVisitorException {
    	System.out.print("while(");
    	node.getExpression().accept(this);
    	System.out.println(")");
    	node.getStatement().accept(this);
    }

    @Override
    public void visit(ForStatement node) throws ASTVisitorException {
    	System.out.print("for(");
    	for(Expression expression : node.getExpressionList1()){
    		expression.accept(this);
    		System.out.print(",");
    	}
    	System.out.print("; ");
    	node.getExpression().accept(this);
    	System.out.print("; ");
    	for(Expression expression : node.getExpressionList2()){
    		expression.accept(this);
    		System.out.print(",");
    	}
    	System.out.print(")");
    	node.getStatement().accept(this);
    }

    @Override
    public void visit(ReturnStatement node) throws ASTVisitorException {
    	System.out.print("return ");
    	node.getExpression().accept(this);
    	System.out.println(";");
    }
}