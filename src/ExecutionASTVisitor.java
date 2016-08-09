
import ast.ASTVisitor;
import ast.ASTVisitorException;
import ast.AnonymousFunctionCall;
import ast.ArrayDef;
import ast.AssignmentExpression;
import ast.BinaryExpression;
import ast.Operator;
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
import ast.utils.ASTUtils;

import environment.EnvironmentStack;

import symbols.value.Value;
import symbols.value.Value_t;

import libraryFunctions.LibraryFunction_t;

import java.util.ArrayList;
import java.util.HashMap;
import libraryFunctions.LibraryFunctions;
import static utils.Constants.LIBRARY_FUNC_ARG;

public class ExecutionASTVisitor implements ASTVisitor {

    private final EnvironmentStack _envStack;
    private int _scope;
    private int _inFunction;
    private int _inLoop;

    private void enterScopeSpace() {
        System.out.println("EnterScopeSpace");
        _scope++;
        _envStack.enterBlock(_scope);
    }

    private void exitScopeSpace() {
        System.out.println("ExitScopeSpace");

        _envStack.exitBlock();
        _scope--;
    }

    private void enterFunctionSpace() {
        System.out.println("EnterFunctionSpace");
        _inFunction++;
        _envStack.enterFunction();
        enterScopeSpace();
    }

    private Value exitFunctionSpace() {
        System.out.println("ExitFunctionSpace");
        _inFunction--;
        return _envStack.exitFunction();
    }

    private void enterLoopSpace() {
        System.out.println("EnterLoopSpace");
        _inLoop++;
    }

    private void exitLoopSpace() {
        System.out.println("ExitLoopSpace");
        _inLoop--;
    }

    public ExecutionASTVisitor() {
        _envStack = new EnvironmentStack();
        _scope = 0;
        _inFunction = 0;
        _inLoop = 0;
    }

    @Override
    public Value visit(Program node) throws ASTVisitorException {
        System.out.println("-Program");
        for (Statement stmt : node.getStatements()) {
            if (stmt != null) {
                stmt.accept(this);
            }
        }

        return null;
    }

    @Override
    public Value visit(ExpressionStatement node) throws ASTVisitorException {
        System.out.println("-ExpressionStatement");
        Value val = null;
        val = node.getExpression().accept(this);
        return null;
    }

    @Override
    public Value visit(AssignmentExpression node) throws ASTVisitorException {
        System.out.println("-AssignmentExpression");
        Value left = node.getLvalue().accept(this);
        Value right = node.getExpression().accept(this);
        return null;
    }

    @Override
    public Value visit(BinaryExpression node) throws ASTVisitorException {
        System.out.println("-BinaryExpression");
        Value result = null;
        Value left = node.getExpression1().accept(this);
        Value right = node.getExpression2().accept(this);
        Operator op = node.getOperator();

        

        String typeError = "Incompatible operand types for '"+node.getOperator()+"': "+left.getType()+" and "+right.getType();
        if((!left.isNumeric() || !right.isNumeric()) && (!left.isBoolean() || !right.isBoolean())){
            if(left.isString() && right.isString() && op.equals(Operator.PLUS)){
                result = new Value(Value_t.STRING, (String)left.getData()+(String)right.getData());
            }else{
                ASTUtils.error(node, typeError);
            }
        }

        if(left.isNumeric()){
            Double leftVal = (left.isReal()) ? (Double) left.getData() : ((Integer) left.getData()).doubleValue();
            Double rightVal = (right.isReal()) ? (Double) right.getData() : ((Integer) right.getData()).doubleValue();
            Double resultVal;
            boolean realResult = false;
            
            if(op.isLogical()){
                if(op.equals(Operator.CMP_EQUAL))
                    result = new Value<Boolean>(Value_t.BOOLEAN, (Double.compare(leftVal, rightVal) == 0));
                else if(op.equals(Operator.NOT_EQUAL))
                    result = new Value<Boolean>(Value_t.BOOLEAN, (Double.compare(leftVal, rightVal) != 0));
                else if(op.equals(Operator.GREATER_OR_EQUAL))
                    result = new Value<Boolean>(Value_t.BOOLEAN, (leftVal >= rightVal));
                else if(op.equals(Operator.GREATER))
                    result = new Value<Boolean>(Value_t.BOOLEAN, (leftVal > rightVal));
                else if(op.equals(Operator.LESS_OR_EQUAL))
                    result = new Value<Boolean>(Value_t.BOOLEAN, (leftVal <= rightVal));
                else if(op.equals(Operator.LESS))
                    result = new Value<Boolean>(Value_t.BOOLEAN, (leftVal < rightVal));
                else
                    ASTUtils.error(node, typeError);
            }else{
                realResult = (left.isReal() || right.isReal()) ? true : false;
                if(op.equals(Operator.PLUS))
                    result = (realResult) ? new Value<Double>(Value_t.REAL, leftVal + rightVal)
                                          : new Value<Integer>(Value_t.INTEGER, (Integer)((Double)(leftVal + rightVal)).intValue());
                else if(op.equals(Operator.MINUS))
                    result = (realResult) ? new Value<Double>(Value_t.REAL, leftVal - rightVal)
                                          : new Value<Integer>(Value_t.INTEGER, (Integer)((Double)(leftVal - rightVal)).intValue());
                else if(op.equals(Operator.MUL))
                    result = (realResult) ? new Value<Double>(Value_t.REAL, leftVal * rightVal)
                                          : new Value<Integer>(Value_t.INTEGER, (Integer)((Double)(leftVal * rightVal)).intValue());
                else if(op.equals(Operator.DIV))
                    result = (realResult) ? new Value<Double>(Value_t.REAL, leftVal / rightVal)
                                          : new Value<Integer>(Value_t.INTEGER, (Integer)((Double)(leftVal / rightVal)).intValue());
                else if(op.equals(Operator.MOD))
                    result = (realResult) ? new Value<Double>(Value_t.REAL, leftVal%rightVal)
                                          : new Value<Integer>(Value_t.INTEGER, (Integer)((Double)(leftVal % rightVal)).intValue());
                }
        }else{
            // Boolean values
            Boolean leftVal = (Boolean) left.getData();
            Boolean rightVal = (Boolean) right.getData();
            if(op.equals(Operator.LOGIC_AND))
                result = new Value<Boolean>(Value_t.BOOLEAN, (leftVal && rightVal));
            else if(op.equals(Operator.LOGIC_OR))
                result = new Value<Boolean>(Value_t.BOOLEAN, (leftVal || rightVal));
            else if(op.equals(Operator.CMP_EQUAL))
                result = new Value<Boolean>(Value_t.BOOLEAN, (leftVal == rightVal));
            else if(op.equals(Operator.NOT_EQUAL))
                result = new Value<Boolean>(Value_t.BOOLEAN, (leftVal != rightVal));
            else
                ASTUtils.error(node, typeError);
        }
        // System.out.println("RESULT: "+result.getData());
        return result;
    }

    @Override
    public Value visit(TermExpressionStmt node) throws ASTVisitorException {
        System.out.println("-TermExpressionStmt");

        Value result = node.getExpression().accept(this);
        return result;
    }

    @Override
    public Value visit(UnaryExpression node) throws ASTVisitorException {
        System.out.println("-UnaryExpression");

        //System.out.print(node.getOperator());
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        } else {
            if (node.getLvalue() instanceof IdentifierExpression) {
                IdentifierExpression id = (IdentifierExpression) node.getLvalue();
                String name = id.getIdentifier();
                 Value sybmolInfo = _envStack.lookupAll(name);
           if (sybmolInfo!=null) {
                if (sybmolInfo.getType() == Value_t.USER_FUNCTION
                        || sybmolInfo.getType() == Value_t.LIBRARY_FUNCTION) {

                    String msg = "Using function: " + name + " as lvalue.";
                    ASTUtils.error(node, msg);
                }
            }
            }
            node.getLvalue().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(IdentifierExpression node) throws ASTVisitorException {
        System.out.println("-IdentifierExpression");
        String name = node.getIdentifier();
        Value symbolInfo;
        //if variable have :: at the front it is "global"
        if (!node.isLocal()) {
            symbolInfo = _envStack.lookupGlobalScope(name);
            if (symbolInfo == null) {
                String msg = "Global variable: " + name + " doesn't exist";
                ASTUtils.error(node, msg);
            }
        } else {
            symbolInfo = _envStack.lookupAll(name);
            if (symbolInfo == null) {
                symbolInfo = new Value();
                _envStack.insertSymbol(name, symbolInfo);
            }
        }
        System.out.println(symbolInfo);
        return symbolInfo;
    }

    @Override
    public Value visit(Member node) throws ASTVisitorException {
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
        return null;
    }

    @Override
    public Value visit(ExtendedCall node) throws ASTVisitorException {
        System.out.println("-ExtendedCall");

        node.getCall().accept(this);

        node.getExpressionList();
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);
        }
        return null;
    }

    @Override
    public Value visit(LvalueCall node) throws ASTVisitorException {
        System.out.println("-LvalueCall");

        Value function = node.getLvalue().accept(this);
//        if (!function.isUserFunction() && !function.isLibraryFunction()) {
//            String msg = "Function call: Symbol does not a function.";
//            ASTUtils.error(node, msg);
//        }

        enterFunctionSpace();

        if (function.isUserFunction()) {
            //Get Actual Arguments
            Value parameters = node.getCallSuffix().accept(this);
            HashMap<Integer, Value> actualArguments = (HashMap<Integer, Value>) parameters.getData();

            int count = 0;
            ArrayList<IdentifierExpression> arguments = ((FunctionDef) function.getData()).getArguments();

            for (IdentifierExpression argument : arguments) {
                String name = argument.getIdentifier();
                System.out.println(name);
                Value argumentInfo = actualArguments.get(count);
                _envStack.insertSymbol(name, argumentInfo);

                count++;

            }

           ((FunctionDef) function.getData()).getBody().accept(this);
         // System.out.println(returnData.getData());
        } else if (function.isLibraryFunction()) {
            //Get Actual Arguments
            Value parameters = node.getCallSuffix().accept(this);
            HashMap<Integer, Value> actualArguments = (HashMap<Integer, Value>) parameters.getData();

            int count = 0;
            for (int i = 0; i < actualArguments.size(); i++) {
                _envStack.insertSymbol(LIBRARY_FUNC_ARG + i, actualArguments.get(i));
            }
            LibraryFunctions.libraryFunction_print();
        }
        exitFunctionSpace();

        return null;
    }

    @Override
    public Value visit(AnonymousFunctionCall node) throws ASTVisitorException {
        System.out.println("-AnonymousFunctionCall");

        node.getFunctionDef().accept(this);

        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);
        }
        return null;
    }

    @Override
    public Value visit(NormCall node) throws ASTVisitorException {
        System.out.println("-NormCall");
        HashMap<Integer, Value> arguments = new HashMap<>();
        int count = 0;
        for (Expression expression : node.getExpressionList()) {
            Value argValue = expression.accept(this);
            arguments.put(count, argValue);
            System.out.println(argValue);
            count++;
        }

        //Change to fox Table.
        return new Value(Value_t.TABLE, arguments);
    }

    @Override
    public Value visit(MethodCall node) throws ASTVisitorException {
        System.out.println("-MethodCall");

        //System.out.print(".." + node.getIdentifier() + "(");
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);
        }
        return null;
    }

    @Override
    public Value visit(ObjectDefinition node) throws ASTVisitorException {
        System.out.println("-ObjectDefinition");
        HashMap<Value, Value> objectData = new HashMap<>();
        if (!node.getIndexedElementList().isEmpty()) {
            for (IndexedElement indexed : node.getIndexedElementList()) {
               ArrayList <Value> data = (ArrayList <Value>)indexed.accept(this).getData();
                objectData.put(data.get(0), data.get(1));
            }
          
        }

        return new Value(Value_t.OBJECT, objectData);
    }

    @Override
    public Value visit(IndexedElement node) throws ASTVisitorException {
        System.out.println("-IndexedElement");
        ArrayList <Value> objectData = new ArrayList <>();
         objectData.add(node.getExpression1().accept(this));
         objectData.add(node.getExpression2().accept(this));
        return new Value(Value_t.UNDEFINED, objectData);
    }

    @Override
    public Value visit(ArrayDef node) throws ASTVisitorException {
        System.out.println("-ArrayDef");
        HashMap<Integer, Value> arrayData = new HashMap<>();
        int count = 0;
        if (node.getExpressionList() != null) {
            for (Expression expression : node.getExpressionList()) {
            Value argValue = expression.accept(this);
            arrayData.put(count, argValue);
            System.out.println(argValue);
            count++;
            }
        }
        return new Value(Value_t.TABLE, arrayData);
    }

    @Override
    public Value visit(Block node) throws ASTVisitorException {
        System.out.println("-Block");

        enterScopeSpace();
        for (Statement stmt : node.getStatementList()) {
           Value data =  stmt.accept(this);
            if(stmt instanceof ReturnStatement) {  exitScopeSpace(); return data;}
        }
        exitScopeSpace();

        return null;
    }

    @Override
    public Value visit(FunctionDefExpression node) throws ASTVisitorException {
        System.out.println("-FunctionDefExpression");

        node.getFunctionDef().accept(this);
        return null;
    }

    @Override
    public Value visit(FunctionDef node) throws ASTVisitorException {
        System.out.println("-FunctionDef");

        String name = node.getFuncName();
        /*Function Name*/
        Value symbolInfo = _envStack.lookupCurrentScope(name);
        boolean isLibraryFunction = LibraryFunction_t.isLibraryFunction(name);

        if (symbolInfo != null) {
            boolean isUserFunction = symbolInfo.getType() == Value_t.USER_FUNCTION;
            String msg;
            if (isUserFunction) {
                msg = "Redeclaration of User-Function: " + name + ".";

            } else if (isLibraryFunction) {
                msg = "User-Function shadows Library-Function: " + name + ".";

            } else {
                msg = "User-Function already declared as Variable: " + name + ".";
            }
            ASTUtils.error(node, msg);
        } else {
            if (isLibraryFunction) {
                String msg = "User-Function Shadows Library-Function: " + name + ".";
                ASTUtils.error(node, msg);

            }
        }

        symbolInfo = new Value(Value_t.USER_FUNCTION, node);
        _envStack.insertSymbol(name, symbolInfo);

        /*Function ONLY Check Arguments*/
        enterScopeSpace();
        for (IdentifierExpression argument : node.getArguments()) {
            name = argument.getIdentifier();
            symbolInfo = _envStack.lookupCurrentScope(name);

            if (symbolInfo != null) {
                String msg = "Redeclaration of Formal-Argument: " + name + ".";
                ASTUtils.error(node, msg);
            }

            if (LibraryFunction_t.isLibraryFunction(name)) {
                String msg = "Formal-Argument shadows Library-Function: " + name + ".";
                ASTUtils.error(node, msg);
            }
            _envStack.insertSymbol(name);

        }
        exitScopeSpace();

        return symbolInfo;
    }

    @Override
    public Value visit(IntegerLiteral node) throws ASTVisitorException {
        System.out.println("-IntegerLiteral");
        return new Value(Value_t.INTEGER, node.getLiteral());
    }

    @Override
    public Value visit(DoubleLiteral node) throws ASTVisitorException {
        System.out.println("-DoubleLiteral");
        return new Value(Value_t.REAL, node.getLiteral());
    }

    @Override
    public Value visit(StringLiteral node) throws ASTVisitorException {
        System.out.println("-StringLiteral");
        return new Value(Value_t.STRING, node.getLiteral());
    }

    @Override
    public Value visit(NullLiteral node) throws ASTVisitorException {
        System.out.println("-NullLiteral");
        return new Value(Value_t.NULL, null);
    }

    @Override
    public Value visit(TrueLiteral node) throws ASTVisitorException {
        System.out.println("-TrueLiteral");
        return new Value(Value_t.BOOLEAN, Boolean.TRUE);
    }

    @Override
    public Value visit(FalseLiteral node) throws ASTVisitorException {
        System.out.println("-FalseLiteral");
        return new Value(Value_t.BOOLEAN, Boolean.FALSE);
    }

    @Override
    public Value visit(IfStatement node) throws ASTVisitorException {
        System.out.println("-IfStatement");

        node.getExpression().accept(this);

        node.getStatement().accept(this);
        if (node.getElseStatement() != null) {
            node.getElseStatement().accept(this);
        }
        return null;
    }

    @Override
    public Value visit(WhileStatement node) throws ASTVisitorException {
        System.out.println("-WhileStatement");

        node.getExpression().accept(this);
        enterLoopSpace();
        node.getStatement().accept(this);
        exitLoopSpace();
        return null;
    }

    @Override
    public Value visit(ForStatement node) throws ASTVisitorException {
        System.out.println("-ForStatement");

        for (Expression expression : node.getExpressionList1()) {
            expression.accept(this);
        }

        node.getExpression().accept(this);

        for (Expression expression : node.getExpressionList2()) {
            expression.accept(this);
        }
        enterLoopSpace();
        node.getStatement().accept(this);
        exitLoopSpace();
        return null;
    }

    @Override
    public Value visit(BreakStatement node) throws ASTVisitorException {
        System.out.println("-BreakStatement");
        if (_inLoop == 0) {

            ASTUtils.error(node, "Use of 'break' while not in a loop.");
        }
        return null;
    }

    @Override
    public Value visit(ContinueStatement node) throws ASTVisitorException {
        System.out.println("-ContinueStatement");
        if (_inLoop == 0) {

            ASTUtils.error(node, "Use of 'continue' while not in a loop.");
        }
        return null;
    }

    @Override
    public Value visit(ReturnStatement node) throws ASTVisitorException {
        System.out.println("-ReturnStatement");
        if (_inFunction == 0) {

            ASTUtils.error(node, "Use of 'return' while not in a function.");
        }
        if (node.getExpression() != null) {
         node.getExpression().accept(this);
        }
        return null;
    }
}
