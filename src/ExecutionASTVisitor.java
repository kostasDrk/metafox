
import ast.ASTNode;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import symbols.value.Value;
import symbols.value.Value_t;
import symbols.value.StaticVal;
import symbols.value.DynamicVal;

import libraryFunctions.LibraryFunction_t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Constants.BREAK;
import static utils.Constants.CONTINUE;
import static utils.Constants.ENTER_FUNCTION_ENV_INIT_SCOPE;
import static utils.Constants.LIBRARY_FUNC_ARG;

public class ExecutionASTVisitor implements ASTVisitor {

    private final EnvironmentStack _envStack;
    private int _scope;
    private int _inFunction;
    private int _inLoop;

    private void enterScopeSpace() {
        //System.out.println("EnterScopeSpace");
        _scope++;
        _envStack.enterBlock(_scope);
    }

    private void exitScopeSpace() {
        //System.out.println("ExitScopeSpace");

        _envStack.exitBlock();
        _scope--;
    }

    private void enterFunctionSpace() {
        //System.out.println("EnterFunctionSpace");

        _scope = ENTER_FUNCTION_ENV_INIT_SCOPE;
        _inFunction++;
        _envStack.enterFunction();

    }

    private Value exitFunctionSpace() {
        //System.out.println("ExitFunctionSpace");

        _inFunction--;

        Value value = _envStack.exitFunction();
        _scope = _envStack.topEnvScope();

        return value;
    }

    private void enterLoopSpace() {
        //System.out.println("EnterLoopSpace");
        _inLoop++;
    }

    private void exitLoopSpace() {
        //System.out.println("ExitLoopSpace");
        _inLoop--;
    }

    private void setNodeIsLValueIfMember(ASTNode node) {
        //System.out.print("SetNodeIsLValueIfMember: ");
        if (node instanceof Member) {
            ((Member) node).setIsLValue();
            //System.out.println("TRUE");
        } else {
            //System.out.println("FALSE");
        }
    }

    public ExecutionASTVisitor() {
        _envStack = new EnvironmentStack();
        _scope = 0;
        _inFunction = 0;
        _inLoop = 0;

    }

    @Override
    public Value visit(Program node) throws ASTVisitorException {
        //System.out.println("-Program");
        for (Statement stmt : node.getStatements()) {
            if (stmt != null) {
                stmt.accept(this);
            }
        }
        // //System.out.println(_envStack.toString());
        return null;
    }

    @Override
    public Value visit(ExpressionStatement node) throws ASTVisitorException {
        //System.out.println("-ExpressionStatement");
        Value val = null;
        val = node.getExpression().accept(this);
        return null;
    }

    @Override
    public Value visit(AssignmentExpression node) throws ASTVisitorException {
        //System.out.println("-AssignmentExpression");
        setNodeIsLValueIfMember(node.getLvalue());
        Value left = node.getLvalue().accept(this);

        setNodeIsLValueIfMember(node.getExpression());
        Value right = node.getExpression().accept(this);

        _envStack.setValue((DynamicVal) left, right);
        //System.out.println(_envStack.toString());
        return left;
    }

    @Override
    public Value visit(BinaryExpression node) throws ASTVisitorException {
        //System.out.println("-BinaryExpression");
        Value result = null;
        Value left = node.getExpression1().accept(this);
        Value right = node.getExpression2().accept(this);
        Operator op = node.getOperator();

        String leftInfo = (left instanceof DynamicVal) ? ((DynamicVal) left).getErrorInfo() + "(" + left.getType() + ")" : left.getData() + "(" + left.getType() + ")";
        leftInfo = "'" + left.getData() + "' (" + left.getType() + ")";
        String rightInfo = (right instanceof DynamicVal) ? ((DynamicVal) right).getErrorInfo() + "(" + right.getType() + ")" : right.getData() + "(" + right.getType() + ")";
        rightInfo = "'" + right.getData() + "' (" + right.getType() + ")";

        // String typeError = "Incompatible operand types for '" + node.getOperator() + "': " + left.getType() + " and " + right.getType();
        String typeError = "Incompatible operand types for '" + node.getOperator() + "': " + leftInfo + " and " + rightInfo;
        if (left.isUndefined() || left.isNull() || right.isUndefined() || right.isNull()) {
            ASTUtils.error(node, typeError);
        }
        if ((!left.isNumeric() || !right.isNumeric()) && (!left.isBoolean() || !right.isBoolean())) {
            if ((left.isString() || right.isString()) && op.equals(Operator.PLUS)) {
                result = new StaticVal(Value_t.STRING, (String) left.getData().toString() + (String) right.getData().toString());
                // //System.out.println("RESULT: "+result.getData());
                return result;
            } else {
                ASTUtils.error(node, typeError);
            }
        }

        if (left.isNumeric()) {
            Double leftVal = (left.isReal()) ? (Double) left.getData() : ((Integer) left.getData()).doubleValue();
            Double rightVal = (right.isReal()) ? (Double) right.getData() : ((Integer) right.getData()).doubleValue();
            Double resultVal;
            boolean realResult;

            if (op.isLogical()) {
                if (op.equals(Operator.CMP_EQUAL)) {
                    result = new StaticVal<>(Value_t.BOOLEAN, (Double.compare(leftVal, rightVal) == 0));
                } else if (op.equals(Operator.NOT_EQUAL)) {
                    result = new StaticVal<>(Value_t.BOOLEAN, (Double.compare(leftVal, rightVal) != 0));
                } else if (op.equals(Operator.GREATER_OR_EQUAL)) {
                    result = new StaticVal<>(Value_t.BOOLEAN, (leftVal >= rightVal));
                } else if (op.equals(Operator.GREATER)) {
                    result = new StaticVal<>(Value_t.BOOLEAN, (leftVal > rightVal));
                } else if (op.equals(Operator.LESS_OR_EQUAL)) {
                    result = new StaticVal<>(Value_t.BOOLEAN, (leftVal <= rightVal));
                } else if (op.equals(Operator.LESS)) {
                    result = new StaticVal<>(Value_t.BOOLEAN, (leftVal < rightVal));
                } else {
                    ASTUtils.error(node, typeError);
                }
            } else {
                realResult = (left.isReal() || right.isReal());
                if (op.equals(Operator.PLUS)) {
                    result = (realResult) ? new StaticVal<>(Value_t.REAL, leftVal + rightVal)
                            : new StaticVal<>(Value_t.INTEGER, (Integer) ((Double) (leftVal + rightVal)).intValue());
                } else if (op.equals(Operator.MINUS)) {
                    result = (realResult) ? new StaticVal<>(Value_t.REAL, leftVal - rightVal)
                            : new StaticVal<>(Value_t.INTEGER, (Integer) ((Double) (leftVal - rightVal)).intValue());
                } else if (op.equals(Operator.MUL)) {
                    result = (realResult) ? new StaticVal<>(Value_t.REAL, leftVal * rightVal)
                            : new StaticVal<>(Value_t.INTEGER, (Integer) ((Double) (leftVal * rightVal)).intValue());
                } else if (op.equals(Operator.DIV)) {
                    result = (realResult) ? new StaticVal<>(Value_t.REAL, leftVal / rightVal)
                            : new StaticVal<>(Value_t.INTEGER, (Integer) ((Double) (leftVal / rightVal)).intValue());
                } else if (op.equals(Operator.MOD)) {
                    result = (realResult) ? new StaticVal<>(Value_t.REAL, leftVal % rightVal)
                            : new StaticVal<>(Value_t.INTEGER, (Integer) ((Double) (leftVal % rightVal)).intValue());
                }
            }
        } else {
            // Boolean values
            Boolean leftVal = (Boolean) left.getData();
            Boolean rightVal = (Boolean) right.getData();
            if (op.equals(Operator.LOGIC_AND)) {
                result = new StaticVal<>(Value_t.BOOLEAN, (leftVal && rightVal));
            } else if (op.equals(Operator.LOGIC_OR)) {
                result = new StaticVal<>(Value_t.BOOLEAN, (leftVal || rightVal));
            } else if (op.equals(Operator.CMP_EQUAL)) {
                result = new StaticVal<>(Value_t.BOOLEAN, (Objects.equals(leftVal, rightVal)));
            } else if (op.equals(Operator.NOT_EQUAL)) {
                result = new StaticVal<>(Value_t.BOOLEAN, (!Objects.equals(leftVal, rightVal)));
            } else {
                ASTUtils.error(node, typeError);
            }
        }
        // //System.out.println("RESULT: "+result.getData());
        return result;
    }

    @Override
    public Value visit(TermExpressionStmt node) throws ASTVisitorException {
        //System.out.println("-TermExpressionStmt");

        Value result = node.getExpression().accept(this);
        return result;
    }

    @Override
    public Value visit(UnaryExpression node) throws ASTVisitorException {
        //System.out.println("-UnaryExpression");

        Operator op = node.getOperator();
        Value returnVal = new StaticVal();

        if (node.getExpression() != null) {
            setNodeIsLValueIfMember(node.getExpression());
            Value value = node.getExpression().accept(this);

            if (op.equals(Operator.MINUS)) {
                if (value.isInteger()) {
                    returnVal = new StaticVal(value.getType(), -(int) value.getData());
                } else if (value.isReal()) {
                    returnVal = new StaticVal(value.getType(), -(double) value.getData());
                } else {
                    String msg = "Symbol '" + ((DynamicVal) value).getErrorInfo()
                            + "' should be numeric type for operation '" + op.toString() + "'.";
                    ASTUtils.error(node, msg);
                }
            } else if (op.equals(Operator.LOGIC_NOT)) {
                if (value.isBoolean()) {
                    returnVal = new StaticVal(Value_t.BOOLEAN, !(boolean) value.getData());

                } else if (value.isInteger() || value.isReal() || value.isString() || value.isUserFunction() || value.isLibraryFunction() || value.isTable() || value.isObject()) {
                    returnVal = new StaticVal(Value_t.BOOLEAN, Boolean.FALSE);

                } else if (value.isNull()) {
                    returnVal = new StaticVal(Value_t.BOOLEAN, Boolean.TRUE);

                } else {
                    String msg = "Symbol '" + ((DynamicVal) value).getErrorInfo()
                            + "' can not converted to boolean type.";
                    ASTUtils.error(node, msg);
                }
            }

        } else {
            setNodeIsLValueIfMember(node.getLvalue());
            Value value = node.getLvalue().accept(this);

            StaticVal assignVal = null;
            if (value.isInteger()) {
                int data = op.equals(Operator.PLUS_PLUS) ? (int) value.getData() + 1 : (int) value.getData() - 1;
                assignVal = new StaticVal(value.getType(), data);
            } else if (value.isReal()) {
                double data = op.equals(Operator.PLUS_PLUS) ? (double) value.getData() + 1 : (double) value.getData() - 1;
                assignVal = new StaticVal(value.getType(), data);
            } else {
                String msg = "Symbol '" + ((DynamicVal) value).getErrorInfo()
                        + "' should be numeric type for operation '" + op.toString() + "'.";
                ASTUtils.error(node, msg);
            }

            _envStack.setValue((DynamicVal) value, assignVal);

        }

        return returnVal;
    }

    @Override
    public Value visit(IdentifierExpression node) throws ASTVisitorException {
        //System.out.println("-IdentifierExpression");
        String name = node.getIdentifier();
        Value symbolInfo;

        //if variable has :: at the front it is "global"
        if (!node.isLocal()) {
            symbolInfo = _envStack.lookupGlobalScope(name);
            if (symbolInfo == null) {
                String msg = "Global variable: " + name + " doesn't exist";
                ASTUtils.error(node, msg);
            }
        } else {
            symbolInfo = _envStack.lookupAll(name);
            if (symbolInfo == null) {
                _envStack.insertSymbol(name);
                symbolInfo = _envStack.lookupCurrentScope(name); // Retrieve newly added symbol
            }
        }
        return symbolInfo;
    }

    @Override
    public Value visit(Member node) throws ASTVisitorException {
        //System.out.println("-Member");

        Value lvalue = null;
        Value key = null;
        Value retVal;

        String id = node.getIdentifier();
        if ((node.getLvalue() != null) && (id != null)) { // lvalue.id 
            lvalue = node.getLvalue().accept(this);
            if (!lvalue.isObject()) {
                String msg = "'" + ((DynamicVal) lvalue).getErrorInfo() + "' is not Object type to get member '" + id + "'.";
                ASTUtils.error(node, msg);
            }

            key = _envStack.lookupAll(node.getIdentifier());

        } else if ((node.getLvalue() != null) && (node.getExpression() != null)) { // lvalue (exp)
            lvalue = node.getLvalue().accept(this);
            if (!lvalue.isObject() && !lvalue.isTable()) {
                String msg = "'" + ((DynamicVal) lvalue).getErrorInfo() + "' is not Object or Array type to get member '" + id + "'.";
                ASTUtils.error(node, msg);
            }

            key = node.getExpression().accept(this);

        } else if ((id != null) && (node.getCall() != null)) { // call.id
            lvalue = node.getCall().accept(this);
            if (!lvalue.isObject()) {
                //Cast error Think about this add all other cases of downcasts of the ExecutionASTVisitor. what to do?
                //String msg = "'" + ((DynamicVal) lvalue).getErrorInfo() + "' it's not Object type to get member '" + id + "'.";
                String msg = "Return value is not Object type to get member '" + id + "'.";
                ASTUtils.error(node, msg);
            }

            key = _envStack.lookupAll(node.getIdentifier());

        } else if ((node.getCall() != null) && (node.getExpression() != null)) { // call (expr)
            lvalue = node.getCall().accept(this);
            if (!lvalue.isObject() && !lvalue.isTable()) {
                //Cast error Think about this add all other cases of downcasts of the ExecutionASTVisitor. what to do?
                //String msg = "'" + ((DynamicVal) lvalue).getErrorInfo() + "' it's not Object or Array type to get member '" + id + "'.";
                String msg = "Return value is not Object or Array type to get member '" + id + "'.";
                ASTUtils.error(node, msg);
            }

            key = node.getExpression().accept(this);

        } else {
            //fatal error Think how to manage this errors.
        }

        HashMap<Value, Value> objectData = (HashMap<Value, Value>) lvalue.getData();
        Value value = objectData.get(key);

        if (value == null) {
            if (node.isLValue()) {
                String errorInfo = "Object." + "id.";

                retVal = new DynamicVal(errorInfo);
                objectData.put(key, retVal);
            } else {
                retVal = new StaticVal(Value_t.NULL, null);
            }

        } else {
            retVal = value;
        }

        return retVal;
    }

    @Override
    public Value visit(ExtendedCall node) throws ASTVisitorException {
        //System.out.println("-ExtendedCall");

        node.getCall().accept(this);

        Value arguments = node.getNormCall().accept(this);
        return null;
    }

    @Override
    public Value visit(LvalueCall node) throws ASTVisitorException {
        //System.out.println("-LvalueCall");

        Value lvalue = node.getLvalue().accept(this);
        //System.out.println(lvalue);
        if (node.getCallSuffix() instanceof NormCall) {
            if (!lvalue.isUserFunction() && !lvalue.isLibraryFunction()) {
                String msg = "Function call: Symbol-" + ((DynamicVal) lvalue).getErrorInfo()
                        + " is not Type-Function, but Type-" + lvalue.getType() + ".";
                ASTUtils.error(node, msg);

            }
        } else if (node.getCallSuffix() instanceof MethodCall) {
            if (!lvalue.isObject()) {
                String msg = "Function call using lvalue..id(elist): Symbol-" + ((DynamicVal) lvalue).getErrorInfo()
                        + " is not Type-Object, but Type-" + lvalue.getType() + ".";
                ASTUtils.error(node, msg);
            }
        }

        if (lvalue.isUserFunction()) {
            //Get Actual Arguments
            Value parameters = node.getCallSuffix().accept(this);
            HashMap<Integer, Value> actualArguments = (HashMap<Integer, Value>) parameters.getData();

            enterFunctionSpace();
            int count = 0;
            ArrayList<IdentifierExpression> arguments = ((FunctionDef) lvalue.getData()).getArguments();

            if (arguments.size() != actualArguments.size()) {
                String msg = "Call to '" + ((DynamicVal) lvalue).getErrorInfo() + "' requires " + arguments.size() + " arguments"
                        + ": " + actualArguments.size() + " found.";
                ASTUtils.error(node, msg);
            }
            for (IdentifierExpression argument : arguments) {
                String name = argument.getIdentifier();
                //System.out.println(name);

                String errorInfo = name;
                DynamicVal argumentInfo = new DynamicVal(actualArguments.get(count), errorInfo);
                _envStack.insertSymbol(name, argumentInfo);

                count++;

            }

            ((FunctionDef) lvalue.getData()).getBody().accept(this);
        } else if (lvalue.isLibraryFunction()) {
            //Get Actual Arguments
            Value parameters = node.getCallSuffix().accept(this);
            HashMap<Integer, Value> actualArguments = (HashMap<Integer, Value>) parameters.getData();

            enterFunctionSpace();
            if (actualArguments.size() < 1) {
                String msg = "Call to '" + ((DynamicVal) lvalue).getErrorInfo() + "' requires at least ONE argument"
                        + ": " + actualArguments.size() + " found.";
                ASTUtils.error(node, msg);
            }

            for (int i = 0; i < actualArguments.size(); i++) {
                String errorInfo = LIBRARY_FUNC_ARG + i;
                DynamicVal argumentInfo = new DynamicVal(actualArguments.get(i), errorInfo);
                _envStack.insertSymbol(LIBRARY_FUNC_ARG + i, argumentInfo);
            }

            try {
                Method method = (Method) lvalue.getData();
                method.invoke(null, _envStack.getFunctionEnv());

            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ExecutionASTVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (lvalue.isObject()) {
            //Get lvalue
            HashMap<Value, Value> objectData = (HashMap<Value, Value>) lvalue.getData();
            //Get ..id
            String id = ((MethodCall) node.getCallSuffix()).getIdentifier();
            Value key = new StaticVal(Value_t.STRING, id);
            Value function = objectData.get(key);

            if (!function.isUserFunction() && !function.isLibraryFunction()) {
                String msg = "Function call using lvalue..id(elist):  Symbmol- 'lvalue." + id
                        + "' is not Type-Function, but Type-" + lvalue.getType() + ".";
                ASTUtils.error(node, msg);
            }

            //Get Actual Arguments
            Value parameters = node.getCallSuffix().accept(this);
            HashMap<Integer, Value> actualArguments = (HashMap<Integer, Value>) parameters.getData();
            ArrayList<IdentifierExpression> arguments = ((FunctionDef) function.getData()).getArguments();

            enterFunctionSpace();
            if (arguments.size() != (actualArguments.size() + 1)) {
                String msg = "Call to '" + ((DynamicVal) lvalue).getErrorInfo() + "' requires " + arguments.size() + " arguments"
                        + ": " + actualArguments.size() + " found.";
                ASTUtils.error(node, msg);
            }

            int count = 0;
            String name = arguments.get(count).getIdentifier();
            String errorInfo = name;
            DynamicVal argumentInfo = new DynamicVal(lvalue, errorInfo);
            _envStack.insertSymbol(name, argumentInfo);

            for (count = 1; count < arguments.size(); count++) {
                name = arguments.get(count).getIdentifier();
                //System.out.println(name);

                errorInfo = name;
                argumentInfo = new DynamicVal(actualArguments.get(count - 1), errorInfo);
                _envStack.insertSymbol(name, argumentInfo);

                count++;

            }

            ((FunctionDef) function.getData()).getBody().accept(this);
        }

        Value ret = exitFunctionSpace();
        if (ret.getType().equals(Value_t.ERROR)) {
            String msg = "Error during Function Execution @'" + ((DynamicVal) lvalue).getErrorInfo() + "'  - " + ret.getData();
            ASTUtils.error(node, msg);
        }

        return ret;
    }

    @Override
    public Value visit(AnonymousFunctionCall node) throws ASTVisitorException {
        //System.out.println("-AnonymousFunctionCall");

        node.getFunctionDef().accept(this);

        Value arguments = node.getNormCall().accept(this);
        
        return null;
    }

    @Override
    public Value visit(NormCall node) throws ASTVisitorException {
        //System.out.println("-NormCall");
        HashMap<Integer, Value> arguments = new HashMap<>();
        int count = 0;
        for (Expression expression : node.getExpressionList()) {
            Value argValue = expression.accept(this);
            arguments.put(count, argValue);
            //System.out.println(argValue);
            count++;
        }

        //Change to fox Table.
        return new StaticVal(Value_t.UNDEFINED, arguments);
    }

    @Override
    public Value visit(MethodCall node) throws ASTVisitorException {
        //System.out.println("-MethodCall");
        Value arguments = node.getNormCall().accept(this);
        return arguments;
    }

    @Override
    public Value visit(ObjectDefinition node) throws ASTVisitorException {
        //System.out.println("-ObjectDefinition");
        HashMap<Value, Value> objectData = new HashMap<>();
        int fieldsNum = node.getIndexedElementList().size();
        Value sizeVal = new StaticVal(Value_t.STRING, "#size");
        Value sizeVal1 = new DynamicVal(Value_t.INTEGER, fieldsNum, "#size");
        objectData.put(sizeVal, sizeVal1);

        if (!node.getIndexedElementList().isEmpty()) {
            for (IndexedElement indexed : node.getIndexedElementList()) {
                ArrayList<Value> data = (ArrayList<Value>) indexed.accept(this).getData();

                String errorInfo = "Object.(" + data.get(0) + ")";
                Value value = new DynamicVal(data.get(1), errorInfo);
                objectData.put(data.get(0), value);
            }
        }

        return new StaticVal(Value_t.OBJECT, objectData);
    }

    @Override
    public Value visit(IndexedElement node) throws ASTVisitorException {
        //System.out.println("-IndexedElement");
        ArrayList<Value> objectData = new ArrayList<>();
        objectData.add(node.getExpression1().accept(this));
        objectData.add(node.getExpression2().accept(this));
        return new StaticVal(Value_t.UNDEFINED, objectData);
    }

    @Override
    public Value visit(ArrayDef node) throws ASTVisitorException {
        //System.out.println("-ArrayDef");
        HashMap<Value, Value> arrayData = new HashMap<>();
        int count = 0;
        if (node.getExpressionList() != null) {
            for (Expression expression : node.getExpressionList()) {
                Value index = new StaticVal(Value_t.INTEGER, count);
                Value value = expression.accept(this);

                String errorInfo = "Object.(" + count + ")";
                Value element = new DynamicVal(value, errorInfo);

                arrayData.put(index, element);
                count++;
            }
        }
        return new StaticVal(Value_t.TABLE, arrayData);
    }

    @Override
    public Value visit(Block node) throws ASTVisitorException {
        //System.out.println("-Block");
        Value ret = null;

        enterScopeSpace();
        for (Statement stmt : node.getStatementList()) {
            ret = stmt.accept(this);
            if (ret != null) {
                if (ret.getData().equals(BREAK) || ret.getData().equals(CONTINUE)) {
                    return ret;
                }
            }
        }
        exitScopeSpace();

        return ret;
    }

    @Override
    public Value visit(FunctionDefExpression node) throws ASTVisitorException {
        //System.out.println("-FunctionDefExpression");

        node.getFunctionDef().accept(this);
        return null;
    }

    @Override
    public Value visit(FunctionDef node) throws ASTVisitorException {
        //System.out.println("-FunctionDef");

        String name = node.getFuncName();
        /*Function Name*/
        DynamicVal symbolInfo = _envStack.lookupCurrentScope(name);
        boolean isLibraryFunction = LibraryFunction_t.isLibraryFunction(name);

        if (symbolInfo != null) {
            boolean isUserFunction = symbolInfo.getType() == Value_t.USER_FUNCTION;
            String msg;
            if (isUserFunction) {
                msg = "Redeclaration of User-Function: " + name + ".";
            } else if (isLibraryFunction) {
                msg = "User-Function shadows Library-Function: " + name + ".";
            } else {
                //Think to remove this else statment.
                msg = "User-Function already declared as Variable: " + name + ".";
            }
            ASTUtils.error(node, msg);
        } else {
            if (isLibraryFunction) {
                String msg = "User-Function Shadows Library-Function: " + name + ".";
                ASTUtils.error(node, msg);
            }
        }

        String errorInfo = name;
        symbolInfo = new DynamicVal(Value_t.USER_FUNCTION, node, errorInfo);
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
        //System.out.println("-IntegerLiteral");
        return new StaticVal(Value_t.INTEGER, node.getLiteral());
    }

    @Override
    public Value visit(DoubleLiteral node) throws ASTVisitorException {
        //System.out.println("-DoubleLiteral");
        return new StaticVal(Value_t.REAL, node.getLiteral());
    }

    @Override
    public Value visit(StringLiteral node) throws ASTVisitorException {
        //System.out.println("-StringLiteral");
        return new StaticVal(Value_t.STRING, node.getLiteral());
    }

    @Override
    public Value visit(NullLiteral node) throws ASTVisitorException {
        //System.out.println("-NullLiteral");
        return new StaticVal(Value_t.NULL, null);
    }

    @Override
    public Value visit(TrueLiteral node) throws ASTVisitorException {
        //System.out.println("-TrueLiteral");
        return new StaticVal(Value_t.BOOLEAN, Boolean.TRUE);
    }

    @Override
    public Value visit(FalseLiteral node) throws ASTVisitorException {
        //System.out.println("-FalseLiteral");
        return new StaticVal(Value_t.BOOLEAN, Boolean.FALSE);
    }

    @Override
    public Value visit(IfStatement node) throws ASTVisitorException {
        //System.out.println("-IfStatement");

        Value val = node.getExpression().accept(this);
        Value ret = null;

        if ((Boolean) val.getData()) {
            ret = node.getStatement().accept(this);
            if (ret != null) {
                if (ret.getData().equals(BREAK) || ret.getData().equals(CONTINUE)) {
                    return ret;
                }
            }
        } else {
            if (node.getElseStatement() != null) {
                ret = node.getElseStatement().accept(this);
                if (ret != null) {
                    if (ret.getData().equals(BREAK) || ret.getData().equals(CONTINUE)) {
                        return ret;
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public Value visit(WhileStatement node) throws ASTVisitorException {
        //System.out.println("-WhileStatement");

        Value val = node.getExpression().accept(this);
        Value ret;

        enterLoopSpace();
        while ((Boolean) ((Value) node.getExpression().accept(this)).getData()) {
            ret = node.getStatement().accept(this);
            if (ret != null) {
                if (ret.getData().equals(BREAK)) {
                    break;
                } else if (ret.getData().equals(CONTINUE)) {
                    continue;
                }
            }
        }
        exitLoopSpace();
        return null;
    }

    @Override
    public Value visit(ForStatement node) throws ASTVisitorException {
        //System.out.println("-ForStatement");

        Value ret;

        enterLoopSpace();
        for (Expression expression : node.getExpressionList1()) {
            expression.accept(this);
        }

        while ((Boolean) ((Value) node.getExpression().accept(this)).getData()) {
            ret = node.getStatement().accept(this);

            if (ret != null) {
                if (ret.getData().equals(BREAK)) {
                    break;
                } else if (ret.getData().equals(CONTINUE)) {
                    for (Expression expression : node.getExpressionList2()) {
                        expression.accept(this);
                    }
                    continue;
                }
            }
            for (Expression expression : node.getExpressionList2()) {
                expression.accept(this);
            }
        }
        exitLoopSpace();

        return null;
    }

    @Override
    public Value visit(BreakStatement node) throws ASTVisitorException {
        //System.out.println("-BreakStatement");
        if (_inLoop == 0) {
            ASTUtils.error(node, "Use of 'break' while not in a loop.");
        }
        return new StaticVal(Value_t.STRING, BREAK);
    }

    @Override
    public Value visit(ContinueStatement node) throws ASTVisitorException {
        //System.out.println("-ContinueStatement");
        if (_inLoop == 0) {
            ASTUtils.error(node, "Use of 'continue' while not in a loop.");
        }
        return new StaticVal(Value_t.STRING, CONTINUE);
    }

    @Override
    public Value visit(ReturnStatement node) throws ASTVisitorException {
        //System.out.println("-ReturnStatement");
        Value ret = null;

        if (_inFunction == 0) {
            ASTUtils.error(node, "Use of 'return' while not in a function.");
        }
        if (node.getExpression() != null) {
            ret = node.getExpression().accept(this);
            _envStack.setReturnValue(ret);
        }
        return ret;
    }
}
