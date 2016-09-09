package ast.visitors;

import ast.ASTVisitor;
import ast.ASTVisitorException;

import ast.ASTNode;
import ast.Program;
import ast.Statement;
import ast.IfStatement;
import ast.WhileStatement;
import ast.ForStatement;
import ast.BreakStatement;
import ast.ContinueStatement;
import ast.ReturnStatement;
import ast.Expression;
import ast.ExpressionStatement;
import ast.AssignmentExpression;
import ast.BinaryExpression;
import ast.UnaryExpression;
import ast.TermExpression;
import ast.ParenthesisExpression;
import ast.IdentifierExpression;
import ast.IdentifierExpressionLocal;
import ast.IdentifierExpressionGlobal;
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
import ast.MetaSyntax;
import ast.MetaEscape;
import ast.MetaEval;
import ast.MetaExecute;
import ast.MetaRun;
import ast.MetaToText;

import symbols.value.Value;
import symbols.value.Value_t;
import symbols.value.StaticVal;

import java.util.ArrayList;

public class IteratorASTVisitor implements ASTVisitor {

	private ArrayList<Statement> _statementList;
	private int _curItem;

	public IteratorASTVisitor(){
		_statementList = new ArrayList<Statement>();
		_curItem = -1;
	}

	public ArrayList<Statement> getStatementList(){
		return this._statementList;
	}

	public void setStatementList(ArrayList<Statement> statementList){
		this._statementList = statementList;
	}

	public Statement getStatement(int pos){
		return this._statementList.get(pos);
	}

    public ASTNode getNextItem(){
        incCurItem();
        return this._statementList.get(this._curItem);
    }

    public ASTNode getPrevItem(){
        decCurItem();
        return this._statementList.get(this._curItem);
    }

	public int getCurItem(){
		return this._curItem;
	}

	public void setCurItem(int curItem){
		this._curItem = curItem;
	}

	public void incCurItem(){
		this._curItem++;
	}

    public void decCurItem(){
        this._curItem--;
    }

    public boolean hasNext(){
        if(this._curItem >= this._statementList.size()-1)
                return false;
        return true;
    }

    public boolean hasPrev(){
        if(this._curItem <= 0)
                return false;
        return true;
    }

	@Override
	public Value visit(Program node) throws ASTVisitorException {
        for (Statement stmt : node.getStatements()) {
            if (stmt != null) {
            	this._statementList.add(stmt);
            }
        }
        return null;
    }

    @Override
    public Value visit(ExpressionStatement node) throws ASTVisitorException {
        if (node.getExpression() != null) {
        }
        return null;
    }

    @Override
    public Value visit(AssignmentExpression node) throws ASTVisitorException {
        // node.getLvalue().accept(this);
        // node.getExpression().accept(this);
        return null;
    }

    @Override
    public Value visit(BinaryExpression node) throws ASTVisitorException {
        // node.getExpression1().accept(this);
        // node.getExpression2().accept(this);
        return null;
    }

    @Override
    public Value visit(ParenthesisExpression node) throws ASTVisitorException {
        // node.getExpression().accept(this);
        return null;
    }

    @Override
    public Value visit(UnaryExpression node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        } else {
            // node.getLvalue().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(IdentifierExpression node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(IdentifierExpressionLocal node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(IdentifierExpressionGlobal node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(Member node) throws ASTVisitorException {
        if (node.getLvalue() != null) {
            // node.getLvalue().accept(this);
        } else if (node.getCall() != null) {
            // node.getCall().accept(this);
        }
        if (node.getIdentifier() != null) {
        } else if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(ExtendedCall node) throws ASTVisitorException {
        // node.getCall().accept(this);
        // node.getNormCall().accept(this);

        return null;
    }

    @Override
    public Value visit(LvalueCall node) throws ASTVisitorException {
        // node.getLvalue().accept(this);
        // node.getCallSuffix().accept(this);
        return null;
    }

    @Override
    public Value visit(AnonymousFunctionCall node) throws ASTVisitorException {
        // node.getFunctionDef().accept(this);
        // node.getLvalueCall().accept(this);
        return null;
    }

    @Override
    public Value visit(NormCall node) throws ASTVisitorException {
        for (Expression expression : node.getExpressionList()) {
            // expression.accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MethodCall node) throws ASTVisitorException {
        // node.getNormCall().accept(this);
        return null;
    }

    @Override
    public Value visit(ObjectDefinition node) throws ASTVisitorException {
        if (!node.getIndexedElementList().isEmpty()) {
            for (IndexedElement indexed : node.getIndexedElementList()) {
                // indexed.accept(this);
            }
        }
        return null;
    }

    @Override
    public Value visit(IndexedElement node) throws ASTVisitorException {
        // node.getExpression1().accept(this);
        // node.getExpression2().accept(this);
        return null;
    }

    @Override
    public Value visit(ArrayDef node) throws ASTVisitorException {
        if (node.getExpressionList() != null) {
            for (Expression expression : node.getExpressionList()) {
                // expression.accept(this);
            }
        }
        return null;
    }

    @Override
    public Value visit(Block node) throws ASTVisitorException {
        for (Statement stmt : node.getStatementList()) {
        	this._statementList.add(stmt);
            // stmt.accept(this);
        }
        return null;
    }

    @Override
    public Value visit(FunctionDefExpression node) throws ASTVisitorException {
        // node.getFunctionDef().accept(this);
        return null;
    }

    @Override
    public Value visit(FunctionDef node) throws ASTVisitorException {
        node.getBody().accept(this);
        return null;
    }

    @Override
    public Value visit(IntegerLiteral node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(DoubleLiteral node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(StringLiteral node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(NullLiteral node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(TrueLiteral node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(FalseLiteral node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(IfStatement node) throws ASTVisitorException {
        // node.getExpression().accept(this);
        if(node.getStatement() instanceof Block)
        	node.getStatement().accept(this);
        else
        	this._statementList.add(node.getStatement());
        if (node.getElseStatement() != null) {
        	if(node.getElseStatement() instanceof Block)
            	node.getElseStatement().accept(this);
            else
            	this._statementList.add(node.getElseStatement());
        }
        return null;
    }

    @Override
    public Value visit(WhileStatement node) throws ASTVisitorException {
        // node.getExpression().accept(this);
        if(node.getStatement() instanceof Block)
        	node.getStatement().accept(this);
        else
        	this._statementList.add(node.getStatement());
        return null;
    }

    @Override
    public Value visit(ForStatement node) throws ASTVisitorException {
        for (Expression expression : node.getExpressionList1()) {
            // expression.accept(this);
        }

        // node.getExpression().accept(this);

        for (Expression expression : node.getExpressionList2()) {
            // expression.accept(this);
        }

        if(node.getStatement() instanceof Block)
        	node.getStatement().accept(this);
        else
        	this._statementList.add(node.getStatement());
        return null;
    }

    @Override
    public Value visit(BreakStatement node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(ContinueStatement node) throws ASTVisitorException {
        return null;
    }

    @Override
    public Value visit(ReturnStatement node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaSyntax node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaEscape node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaExecute node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaRun node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaEval node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaToText node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
        return null;
    }

}