package ast.visitors;

import ast.ASTVisitor;
import ast.ASTVisitorException;
import symbols.value.Value;
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
import ast.TermExpressionStmt;
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
import symbols.value.StaticVal;
import symbols.value.Value_t;

public class ToStringASTVisitor implements ASTVisitor {

    private final StringBuilder _programm;

    public ToStringASTVisitor() {
        _programm = new StringBuilder();
    }

    @Override
    public String toString() {
        return _programm.toString();
    }

    private void semicolon() {
        _programm.append(";").append("\n");
    }

    @Override
    public Value visit(Program node) throws ASTVisitorException {
        for (Statement stmt : node.getStatements()) {
            if (stmt != null) {
                stmt.accept(this);
            }
        }
        return null;
    }

    @Override
    public Value visit(ExpressionStatement node) throws ASTVisitorException {
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        semicolon();

        return null;
    }

    @Override
    public Value visit(AssignmentExpression node) throws ASTVisitorException {
        node.getLvalue().accept(this);
        _programm.append(" = ");
        node.getExpression().accept(this);
        return null;
    }

    @Override
    public Value visit(BinaryExpression node) throws ASTVisitorException {
        node.getExpression1().accept(this);
        _programm.append(" ").append(node.getOperator()).append(" ");
        node.getExpression2().accept(this);
        return null;
    }

    @Override
    public Value visit(TermExpressionStmt node) throws ASTVisitorException {
        _programm.append("(");
        node.getExpression().accept(this);
        _programm.append(")");
        return null;
    }

    @Override
    public Value visit(UnaryExpression node) throws ASTVisitorException {
        _programm.append(node.getOperator());
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        } else {
            node.getLvalue().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(IdentifierExpression node) throws ASTVisitorException {
        _programm.append(node.getIdentifier());

        return null;
    }

    @Override
    public Value visit(IdentifierExpressionLocal node) throws ASTVisitorException {
        _programm.append("local ");
        _programm.append(node.getIdentifier());

        return null;
    }

    @Override
    public Value visit(IdentifierExpressionGlobal node) throws ASTVisitorException {
        _programm.append("::");
        _programm.append(node.getIdentifier());
        
        return null;
    }

    @Override
    public Value visit(Member node) throws ASTVisitorException {
        if (node.getLvalue() != null) {
            node.getLvalue().accept(this);
        } else if (node.getCall() != null) {
            node.getCall().accept(this);
        }
        if (node.getIdentifier() != null) {
            _programm.append(".").append(node.getIdentifier());
        } else if (node.getExpression() != null) {
            _programm.append("[");
            node.getExpression().accept(this);
            _programm.append("]");
        }
        return null;
    }

    @Override
    public Value visit(ExtendedCall node) throws ASTVisitorException {
        node.getCall().accept(this);
        node.getNormCall().accept(this);

        return null;
    }

    @Override
    public Value visit(LvalueCall node) throws ASTVisitorException {
        node.getLvalue().accept(this);
        node.getCallSuffix().accept(this);
        return null;
    }

    @Override
    public Value visit(AnonymousFunctionCall node) throws ASTVisitorException {
        _programm.append("(");
        node.getFunctionDef().accept(this);
        _programm.append(")");
        node.getLvalueCall().accept(this);

        return null;
    }

    @Override
    public Value visit(NormCall node) throws ASTVisitorException {
        _programm.append("( ");
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);
            _programm.append(",");
        }
        _programm.setCharAt(_programm.length() - 1, ' ');
        _programm.append(")");

        return null;
    }

    @Override
    public Value visit(MethodCall node) throws ASTVisitorException {
        _programm.append("..").append(node.getIdentifier());
        node.getNormCall().accept(this);

        return null;
    }

    @Override
    public Value visit(ObjectDefinition node) throws ASTVisitorException {
        _programm.append("{ ");
        if (!node.getIndexedElementList().isEmpty()) {
            for (IndexedElement indexed : node.getIndexedElementList()) {
                indexed.accept(this);
                _programm.append(",\n\t");
            }
            _programm.setCharAt(_programm.length() - 1, ' ');
            _programm.setCharAt(_programm.length() - 2, ' ');
            _programm.setCharAt(_programm.length() - 3, ' ');
        }
        _programm.append("}");
        return null;
    }

    @Override
    public Value visit(IndexedElement node) throws ASTVisitorException {
        node.getExpression1().accept(this);
        _programm.append(" : ");
        node.getExpression2().accept(this);
        return null;
    }

    @Override
    public Value visit(ArrayDef node) throws ASTVisitorException {
        _programm.append("[ ");
        if (node.getExpressionList() != null) {
            for (Expression expression : node.getExpressionList()) {
                expression.accept(this);
                _programm.append(",");
            }
            _programm.setCharAt(_programm.length() - 1, ' ');
        }
        _programm.append("]");
        return null;
    }

    @Override
    public Value visit(Block node) throws ASTVisitorException {
        _programm.append("{").append("\n");
        for (Statement stmt : node.getStatementList()) {
            stmt.accept(this);
        }
        _programm.append("}").append("\n");
        return null;
    }

    @Override
    public Value visit(FunctionDefExpression node) throws ASTVisitorException {
        node.getFunctionDef().accept(this);
        return null;
    }

    @Override
    public Value visit(FunctionDef node) throws ASTVisitorException {
        _programm.append("function ");
        _programm.append(node.getFuncName());
        _programm.append("( ");
        node.getArguments().stream().forEach((id) -> {
            _programm.append(id.getIdentifier()).append(", ");
        });
        _programm.setCharAt(_programm.length() - 1, ')');
        _programm.setCharAt(_programm.length() - 2, ' ');

        node.getBody().accept(this);
        return null;
    }

    @Override
    public Value visit(IntegerLiteral node) throws ASTVisitorException {
        _programm.append(node.getLiteral());
        return new StaticVal(Value_t.INTEGER, node.getLiteral());
    }

    @Override
    public Value visit(DoubleLiteral node) throws ASTVisitorException {
        _programm.append(node.getLiteral());
        return null;
    }

    @Override
    public Value visit(StringLiteral node) throws ASTVisitorException {
        _programm.append("\"").append(node.getLiteral()).append("\"");
        return null;
    }

    @Override
    public Value visit(NullLiteral node) throws ASTVisitorException {
        _programm.append("nil");
        return null;
    }

    @Override
    public Value visit(TrueLiteral node) throws ASTVisitorException {
        _programm.append("true");
        return null;
    }

    @Override
    public Value visit(FalseLiteral node) throws ASTVisitorException {
        _programm.append("false");
        return null;
    }

    @Override
    public Value visit(IfStatement node) throws ASTVisitorException {
        _programm.append("if(");
        node.getExpression().accept(this);
        _programm.append(")").append("\n");
        node.getStatement().accept(this);
        if (node.getElseStatement() != null) {
            _programm.append("else");
            node.getElseStatement().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(WhileStatement node) throws ASTVisitorException {
        _programm.append("while(");
        node.getExpression().accept(this);
        _programm.append(")").append("\n");
        node.getStatement().accept(this);
        return null;
    }

    @Override
    public Value visit(ForStatement node) throws ASTVisitorException {
        _programm.append("for( ");
        for (Expression expression : node.getExpressionList1()) {
            expression.accept(this);
            _programm.append(",");
        }
        _programm.setCharAt(_programm.length() - 1, ' ');
        _programm.append("; ");

        node.getExpression().accept(this);
        _programm.append("; ");

        for (Expression expression : node.getExpressionList2()) {
            expression.accept(this);
            _programm.append(",");
        }
        _programm.setCharAt(_programm.length() - 1, ' ');
        _programm.append(")");

        node.getStatement().accept(this);
        return null;
    }

    @Override
    public Value visit(BreakStatement node) throws ASTVisitorException {
        _programm.append("break;").append("\n");
        return null;
    }

    @Override
    public Value visit(ContinueStatement node) throws ASTVisitorException {
        _programm.append("continue;").append("\n");
        return null;
    }

    @Override
    public Value visit(ReturnStatement node) throws ASTVisitorException {
        _programm.append("return ");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        _programm.append(";").append("\n");
        return null;
    }

    @Override
    public Value visit(MetaSyntax node) throws ASTVisitorException {
        _programm.append("<.").append("\n");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        _programm.append(">.").append("\n");
        return null;
    }

    @Override
    public Value visit(MetaEscape node) throws ASTVisitorException {
        _programm.append(".~");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaExecute node) throws ASTVisitorException {
        _programm.append(".!");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaRun node) throws ASTVisitorException {
        _programm.append(".@");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaEval node) throws ASTVisitorException {
        _programm.append(".eval(");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        _programm.append(")");
        return null;
    }

    @Override
    public Value visit(MetaToText node) throws ASTVisitorException {
        _programm.append(".#");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        return null;
    }

}
