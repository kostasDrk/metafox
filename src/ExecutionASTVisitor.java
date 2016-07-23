
import ast.ASTVisitor;
import ast.ASTVisitorException;
import ast.AnonymousFunctionCall;
import ast.ArrayDef;
import ast.AssignmentExpression;
import ast.BinaryExpression;
import ast.Block;
import ast.BreakStatement;
import ast.ContinueStatement;
import ast.DoubleLiteral;
import ast.Expression;
import ast.ExpressionStatement;
import ast.ExtendedCall;
import ast.FalseLiteral;
import ast.ForStatement;
import ast.FunctionDef;
import ast.FunctionDefExpression;
import ast.IdentifierExpression;
import ast.IfStatement;
import ast.IndexedElement;
import ast.IntegerLiteral;
import ast.LvalueCall;
import ast.Member;
import ast.MethodCall;
import ast.NormCall;
import ast.NullLiteral;
import ast.ObjectDefinition;
import ast.Program;
import ast.ReturnStatement;
import ast.Statement;
import ast.StringLiteral;
import ast.TermExpressionStmt;
import ast.TrueLiteral;
import ast.UnaryExpression;
import ast.WhileStatement;

import symbolTable.SymbolTable;

public class ExecutionASTVisitor implements ASTVisitor {

    private final SymbolTable _symTable;
    private int _scope;

    private void enterScopeSpace() {
        System.out.println("EnterScopeSpace");
        _scope++;
    }

    private void exitScopeSpace() {
        System.out.println("ExitScopeSpace");
        _scope--;
    }

    public ExecutionASTVisitor() {
        _symTable = new SymbolTable();
        _scope = 0;
    }

    @Override
    public void visit(Program node) throws ASTVisitorException {
        System.out.println("-Program");

        for (Statement stmt : node.getStatements()) {
            stmt.accept(this);
        }
    }

    @Override
    public void visit(ExpressionStatement node) throws ASTVisitorException {
        System.out.println("-ExpressionStatement");

        node.getExpression().accept(this);

    }

    @Override
    public void visit(AssignmentExpression node) throws ASTVisitorException {
        System.out.println("-AssignmentExpression");

        node.getLvalue().accept(this);
        node.getExpression().accept(this);
    }

    @Override
    public void visit(BinaryExpression node) throws ASTVisitorException {
        System.out.println("-BinaryExpression");

        node.getExpression1().accept(this);
        //System.out.print(" " + node.getOperator() + " ");
        node.getExpression2().accept(this);
    }

    @Override
    public void visit(TermExpressionStmt node) throws ASTVisitorException {
        System.out.println("-TermExpressionStmt");

        node.getExpression().accept(this);

    }

    @Override
    public void visit(UnaryExpression node) throws ASTVisitorException {
        System.out.println("-UnaryExpression");
        
        //System.out.print(node.getOperator());
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        } else {
            node.getLvalue().accept(this);
        }
    }

    @Override
    public void visit(IdentifierExpression node) throws ASTVisitorException {
        System.out.println("-IdentifierExpression");
        
        if (!node.isLocal()) {
            //_symTable.lookupGlobalScope(node.getIdentifier(), node.getLine());
        } else {

        }
        //System.out.print(node.getIdentifier());
    }

    @Override
    public void visit(Member node) throws ASTVisitorException {
        System.out.println("-Member");
        
        if (node.getLvalue() != null) {
            node.getLvalue().accept(this);
            
        } else if (node.getCall() != null) {
            node.getCall().accept(this);
        }
        if (node.getIdentifier() != null) {
            //System.out.print("." + node.getIdentifier());
            
        } else if (node.getExpression() != null) {
            node.getExpression().accept(this);

        }
    }

    @Override
    public void visit(ExtendedCall node) throws ASTVisitorException {
        System.out.println("-ExtendedCall");
        
        node.getCall().accept(this);

        node.getExpressionList();
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);

        }

    }

    @Override
    public void visit(LvalueCall node) throws ASTVisitorException {
        System.out.println("-LvalueCall");
        
        node.getLvalue().accept(this);
        node.getCallSuffix().accept(this);
    }

    @Override
    public void visit(AnonymousFunctionCall node) throws ASTVisitorException {
        System.out.println("-AnonymousFunctionCall");
        
        node.getFunctionDef().accept(this);

        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);
        }

    }

    @Override
    public void visit(NormCall node) throws ASTVisitorException {
        System.out.println("-NormCall");
        
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);

        }

    }

    @Override
    public void visit(MethodCall node) throws ASTVisitorException {
        System.out.println("-MethodCall");
        
        //System.out.print(".." + node.getIdentifier() + "(");
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);

        }

    }

    @Override
    public void visit(ObjectDefinition node) throws ASTVisitorException {
        System.out.println("-ObjectDefinition");
        
        if (!node.getIndexedElementList().isEmpty()) {
            for (IndexedElement indexed : node.getIndexedElementList()) {
                indexed.accept(this);

            }
        }

    }

    @Override
    public void visit(IndexedElement node) throws ASTVisitorException {
        System.out.println("-IndexedElement");
        
        node.getExpression1().accept(this);
        node.getExpression2().accept(this);
    }

    @Override
    public void visit(ArrayDef node) throws ASTVisitorException {
        System.out.println("-ArrayDef");
        
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);

        }

    }

    @Override
    public void visit(Block node) throws ASTVisitorException {
        System.out.println("-Block");
        
        enterScopeSpace();
        for (Statement stmt : node.getStatementList()) {
            stmt.accept(this);
        }
        exitScopeSpace();
    }

    @Override
    public void visit(FunctionDefExpression node) throws ASTVisitorException {
        System.out.println("-FunctionDefExpression");
        
        node.getFunctionDef().accept(this);
    }

    @Override
    public void visit(FunctionDef node) throws ASTVisitorException {
        System.out.println("-FunctionDef");
        
        //System.out.print(node.getFuncName());

        enterScopeSpace();
        for (String id : node.getArguments()) {
            /*code to be add*/
        }
        exitScopeSpace();
        
        node.getBody().accept(this);

    }

    @Override
    public void visit(IntegerLiteral node) throws ASTVisitorException {
        System.out.println("-IntegerLiteral");
        
    }

    @Override
    public void visit(DoubleLiteral node) throws ASTVisitorException {
        System.out.println("-DoubleLiteral");

    }

    @Override
    public void visit(StringLiteral node) throws ASTVisitorException {
        System.out.println("-StringLiteral");

    }

    @Override
    public void visit(NullLiteral node) throws ASTVisitorException {
        System.out.println("-NullLiteral");

    }

    @Override
    public void visit(TrueLiteral node) throws ASTVisitorException {
        System.out.println("-TrueLiteral");

    }

    @Override
    public void visit(FalseLiteral node) throws ASTVisitorException {
        System.out.println("-FalseLiteral");

    }

    @Override
    public void visit(IfStatement node) throws ASTVisitorException {
        System.out.println("-IfStatement");
        
        node.getExpression().accept(this);

        node.getStatement().accept(this);
        if (node.getElseStatement() != null) {
            node.getElseStatement().accept(this);
        }
    }

    @Override
    public void visit(WhileStatement node) throws ASTVisitorException {
        System.out.println("-WhileStatement");
        
        node.getExpression().accept(this);
        node.getStatement().accept(this);
    }

    @Override
    public void visit(ForStatement node) throws ASTVisitorException {
        System.out.println("-ForStatement");
        
        for (Expression expression : node.getExpressionList1()) {
            expression.accept(this);
        }
        
        node.getExpression().accept(this);

        for (Expression expression : node.getExpressionList2()) {
            expression.accept(this);
        }
        
        node.getStatement().accept(this);
    }

    @Override
    public void visit(BreakStatement node) throws ASTVisitorException {
        System.out.println("-BreakStatement");
 
    }

    @Override
    public void visit(ContinueStatement node) throws ASTVisitorException {
        System.out.println("-ContinueStatement");
    }

    @Override
    public void visit(ReturnStatement node) throws ASTVisitorException {
        System.out.println("-ReturnStatement");
        
        node.getExpression().accept(this);
    }
}
