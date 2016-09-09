package ast.visitors;

import ast.*;

import symbols.value.Value;

import java.util.ArrayList;

public class IteratorASTVisitor implements ASTVisitor {

    private ArrayList<Statement> _statementList;
    private int _curItem;

    public IteratorASTVisitor() {
        _statementList = new ArrayList<>();
        _curItem = -1;
    }

    public ArrayList<Statement> getStatementList() {
        return _statementList;
    }

    public void setStatementList(ArrayList<Statement> statementList) {
        _statementList = statementList;
    }

    public Statement getStatement(int pos) {
        return _statementList.get(pos);
    }

    public ASTNode getNextItem() {
        incCurItem();
        return _statementList.get(_curItem);
    }

    public ASTNode getPrevItem() {
        decCurItem();
        return _statementList.get(_curItem);
    }

    public int getCurItem() {
        return _curItem;
    }

    public void setCurItem(int curItem) {
        _curItem = curItem;
    }

    public void incCurItem() {
        _curItem++;
    }

    public void decCurItem() {
        _curItem--;
    }

    public boolean hasNext() {
        return _curItem < _statementList.size() - 1;
    }

    public boolean hasPrev() {
        return _curItem > 0;
    }

    @Override
    public Value visit(Program node) throws ASTVisitorException {
        node.getStatements().stream().filter((stmt) -> (stmt != null)).forEach((stmt) -> {
            _statementList.add(stmt);
        });
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
        node.getStatementList().stream().forEach((stmt) -> {
            _statementList.add(stmt);
            // stmt.accept(this);
        });
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
        if (node.getStatement() instanceof Block) {
            node.getStatement().accept(this);
        } else {
            _statementList.add(node.getStatement());
        }
        if (node.getElseStatement() != null) {
            if (node.getElseStatement() instanceof Block) {
                node.getElseStatement().accept(this);
            } else {
                _statementList.add(node.getElseStatement());
            }
        }
        return null;
    }

    @Override
    public Value visit(WhileStatement node) throws ASTVisitorException {
        // node.getExpression().accept(this);
        if (node.getStatement() instanceof Block) {
            node.getStatement().accept(this);
        } else {
            _statementList.add(node.getStatement());
        }
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

        if (node.getStatement() instanceof Block) {
            node.getStatement().accept(this);
        } else {
            _statementList.add(node.getStatement());
        }
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
