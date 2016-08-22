
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
import ast.MetaExecute;
import symbols.value.StaticVal;
import symbols.value.Value_t;

public class PrintASTVisitor implements ASTVisitor {

    private void semicolon() {
        System.out.println(";");
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
        node.getExpression().accept(this);
        semicolon();
        return null;
    }

    @Override
    public Value visit(AssignmentExpression node) throws ASTVisitorException {
        node.getLvalue().accept(this);
        System.out.print(" = ");
        node.getExpression().accept(this);
        return null;
    }

    @Override
    public Value visit(BinaryExpression node) throws ASTVisitorException {
        node.getExpression1().accept(this);
        System.out.print(" " + node.getOperator() + " ");
        node.getExpression2().accept(this);
        return null;
    }

    @Override
    public Value visit(TermExpressionStmt node) throws ASTVisitorException {
        System.out.print("(");
        node.getExpression().accept(this);
        System.out.print(")");
        return null;
    }

    @Override
    public Value visit(UnaryExpression node) throws ASTVisitorException {
        System.out.print(node.getOperator());
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        } else {
            node.getLvalue().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(IdentifierExpression node) throws ASTVisitorException {
        if (!node.isLocal()) {
            System.out.print("::");
        }
        System.out.print(node.getIdentifier());
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
            System.out.print("." + node.getIdentifier());
        } else if (node.getExpression() != null) {
            System.out.print("[");
            node.getExpression().accept(this);
            System.out.print("]");
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
        System.out.print("(");
        node.getFunctionDef().accept(this);
        System.out.print(")");
        node.getLvalueCall().accept(this);
        
        return null;
    }

    @Override
    public Value visit(NormCall node) throws ASTVisitorException {
        System.out.print("(");
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);
            System.out.print(",");
        }
        System.out.print(")");
        return null;
    }

    @Override
    public Value visit(MethodCall node) throws ASTVisitorException {
        System.out.print(".." + node.getIdentifier());
        node.getNormCall().accept(this);
        
        return null;
    }

    @Override
    public Value visit(ObjectDefinition node) throws ASTVisitorException {
        System.out.print("{");
        if (!node.getIndexedElementList().isEmpty()) {
            for (IndexedElement indexed : node.getIndexedElementList()) {
                indexed.accept(this);
                System.out.print(",\n\t");
            }
        }
        System.out.print("}");
        return null;
    }

    @Override
    public Value visit(IndexedElement node) throws ASTVisitorException {
        node.getExpression1().accept(this);
        System.out.print(" : ");
        node.getExpression2().accept(this);
        return null;
    }

    @Override
    public Value visit(ArrayDef node) throws ASTVisitorException {
        System.out.print("[");
        if (node.getExpressionList() != null) {
            for (Expression expression : node.getExpressionList()) {
                expression.accept(this);
                System.out.print(",");
            }
        }
        System.out.print("]");
        return null;
    }

    @Override
    public Value visit(Block node) throws ASTVisitorException {
        System.out.println("{");
        for (Statement stmt : node.getStatementList()) {
            stmt.accept(this);
        }
        System.out.println("}");
        return null;
    }

    @Override
    public Value visit(FunctionDefExpression node) throws ASTVisitorException {
        node.getFunctionDef().accept(this);
        return null;
    }

    @Override
    public Value visit(FunctionDef node) throws ASTVisitorException {
        System.out.print("function ");
        System.out.print(node.getFuncName());
        System.out.print("(");
        for (IdentifierExpression id : node.getArguments()) {
            System.out.print(id.getIdentifier() + ", ");
        }
        System.out.print(")");
        node.getBody().accept(this);
        return null;
    }

    @Override
    public Value visit(IntegerLiteral node) throws ASTVisitorException {
        System.out.print(node.getLiteral());
        return new StaticVal(Value_t.INTEGER, node.getLiteral());
    }

    @Override
    public Value visit(DoubleLiteral node) throws ASTVisitorException {
        System.out.print(node.getLiteral());
        return null;
    }

    @Override
    public Value visit(StringLiteral node) throws ASTVisitorException {
        System.out.print("\"" + node.getLiteral() + "\"");
        return null;
    }

    @Override
    public Value visit(NullLiteral node) throws ASTVisitorException {
        System.out.print("nil");
        return null;
    }

    @Override
    public Value visit(TrueLiteral node) throws ASTVisitorException {
        System.out.print("true");
        return null;
    }

    @Override
    public Value visit(FalseLiteral node) throws ASTVisitorException {
        System.out.print("false");
        return null;
    }

    @Override
    public Value visit(IfStatement node) throws ASTVisitorException {
        System.out.print("if(");
        node.getExpression().accept(this);
        System.out.println(")");
        node.getStatement().accept(this);
        if (node.getElseStatement() != null) {
            System.out.print("else");
            node.getElseStatement().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(WhileStatement node) throws ASTVisitorException {
        System.out.print("while(");
        node.getExpression().accept(this);
        System.out.println(")");
        node.getStatement().accept(this);
        return null;
    }

    @Override
    public Value visit(ForStatement node) throws ASTVisitorException {
        System.out.print("for(");
        for (Expression expression : node.getExpressionList1()) {
            expression.accept(this);
            System.out.print(",");
        }
        System.out.print("; ");
        node.getExpression().accept(this);
        System.out.print("; ");
        for (Expression expression : node.getExpressionList2()) {
            expression.accept(this);
            System.out.print(",");
        }
        System.out.print(")");
        node.getStatement().accept(this);
        return null;
    }

    @Override
    public Value visit(BreakStatement node) throws ASTVisitorException {
        System.out.println("break;");
        return null;
    }

    @Override
    public Value visit(ContinueStatement node) throws ASTVisitorException {
        System.out.println("continue;");
        return null;
    }

    @Override
    public Value visit(ReturnStatement node) throws ASTVisitorException {
        System.out.print("return ");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        System.out.println(";");
        return null;
    }

    @Override
    public Value visit(MetaSyntax node) throws ASTVisitorException {
        System.out.println("<.");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        System.out.println(">.");
        return null;
    }

    @Override
    public Value visit(MetaEscape node) throws ASTVisitorException {
        System.out.println(".~");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(MetaExecute node) throws ASTVisitorException {
        System.out.println(".!");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        return null;
    }

}
