package libraryFunctions;

import ast.ASTNode;
import ast.ASTVisitor;
import ast.ASTVisitorException;
import java.util.ArrayList;

import environment.Environment;
import environment.FunctionEnv;

import ast.Statement;
import ast.ExpressionStatement;
import ast.Block;
import ast.Expression;
import ast.FunctionDef;
import ast.visitors.ToStringASTVisitor;

import dataStructures.FoxObject;
import dataStructures.FoxArray;
import dataStructures.AFoxDataStructure;

import symbols.value.Value;
import symbols.value.Value_t;
import symbols.value.StaticVal;

import static utils.Constants.LIBRARY_FUNC_ARG;

public class LibraryFunctions {

    public static void print(Environment env) throws ASTVisitorException {
        if (!checkArgumentsNum(LibraryFunction_t.PRINT, env)) {
            return;
        }

        int totalActuals = env.totalActuals();
        for (int i = 0; i < totalActuals; i++) {
            String data;

            Value argument = env.getActualArgument(LIBRARY_FUNC_ARG + i);
            if (argument.isUserFunction() || argument.isAST()) {
                ASTNode program = (ASTNode) argument.getData();
                ASTVisitor astVisitor = new ToStringASTVisitor();
                program.accept(astVisitor);

                data = astVisitor.toString();

            } else if (argument.isLibraryFunction()) {
                //Get Library Function Name
                String func = argument.getData().toString()
                        .replace("public static void libraryFunctions.LibraryFunctions.", "")
                        .replace("(environment.Environment)", "")
                        .replace(" throws ast.ASTVisitorException", "");

                //Get Total Arguments
                int totalArgs = LibraryFunction_t.totalArgs(func);

                //Library Function toString
                StringBuilder msg = new StringBuilder();
                msg.append("LibraryFunctions.").append(func).append("( ");
                if (totalArgs == -1) {
                    msg.append("arg0, arg1, arg2, ... )");
                } else if (totalArgs == -2) {
                    msg.append("arg0key, arg0value, arg1key, arg1value, ... )");
                } else {
                    for (int j = 0; i < totalArgs; i++) {
                        msg.append("arg, ");
                    }
                    msg.setCharAt(msg.length() - 1, ')');
                    msg.setCharAt(msg.length() - 2, ' ');
                }

                data = msg.toString();

            } else {
                data = argument.getData().toString();
            }

            System.out.print(data);
        }
    }

    public static void println(Environment env) throws ASTVisitorException {
        if (!checkArgumentsNum(LibraryFunction_t.PRINT_LN, env)) {
            return;
        }

        print(env);
        System.out.println("");
    }

    public static void len(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.LEN, env)) {
            return;
        }

        AFoxDataStructure fdataStructure = getObjectArgument(env);
        if (fdataStructure == null) {
            return;
        }
        StaticVal retVal = new StaticVal(Value_t.INTEGER, fdataStructure.size());
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void keys(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.KEYS, env)) {
            return;
        }

        AFoxDataStructure fobject = getObjectArgument(env);
        if (!(fobject instanceof FoxObject)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be of type Object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        FoxArray farray = new FoxArray(((FoxObject) fobject).keys());

        StaticVal retVal = new StaticVal(Value_t.TABLE, farray);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void values(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.VALUES, env)) {
            return;
        }

        AFoxDataStructure fdataStructure = getObjectArgument(env);
        if (fdataStructure == null) {
            return;
        }
        FoxArray farray = new FoxArray(fdataStructure.values());

        StaticVal retVal = new StaticVal(Value_t.TABLE, farray);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void diagnose(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.DIAGNOSE, env)) {
            return;
        }

        //Prepare environment for Library function call: addFirst
        FunctionEnv tmpEnv = new FunctionEnv();
        tmpEnv.insert(LIBRARY_FUNC_ARG + 0, env.getActualArgument(LIBRARY_FUNC_ARG + 0));
        tmpEnv.insert(LIBRARY_FUNC_ARG + 1, env.getActualArgument(LIBRARY_FUNC_ARG + 1));
        addFirst(tmpEnv);
        ((FunctionEnv) env).setReturnVal(tmpEnv.getReturnVal());
        if (tmpEnv.getReturnVal().getType().equals(Value_t.ERROR)) {
            return;
        }

        //Prepare environment for Library function call: addOnExitPoints
        tmpEnv = new FunctionEnv();
        tmpEnv.insert(LIBRARY_FUNC_ARG + 0, env.getActualArgument(LIBRARY_FUNC_ARG + 0));
        tmpEnv.insert(LIBRARY_FUNC_ARG + 1, env.getActualArgument(LIBRARY_FUNC_ARG + 2));
        addOnExitPoints(tmpEnv);

        ((FunctionEnv) env).setReturnVal(tmpEnv.getReturnVal());
    }

    public static void addFirst(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ADDFIRST, env)) {
            return;
        }

        FunctionDef funcdef = getFunctionArgument(env);
        if (funcdef == null) {
            return;
        }
        Value onEnterValue = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        Block funcBody = funcdef.getBody();

        if (onEnterValue.getData() instanceof Expression) {
            ExpressionStatement exstmtEnter = new ExpressionStatement((Expression) onEnterValue.getData());
            funcBody.prependStatement(exstmtEnter);
        } else if (onEnterValue.getData() instanceof ArrayList<?>) {
            funcBody.prependStatements((ArrayList<Statement>) onEnterValue.getData());
        } else {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be either an expression or a statement list.");
            ((FunctionEnv) env).setReturnVal(retVal);
        }
    }

    public static void addOnExitPoints(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ADDONEXITPOINTS, env)) {
            return;
        }

        FunctionDef funcdef = getFunctionArgument(env);
        if (funcdef == null) {
            return;
        }
        Value onExitValue = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        Block funcBody = funcdef.getBody();

        if (onExitValue.getData() instanceof Expression) {
            ExpressionStatement exstmtExit = new ExpressionStatement((Expression) onExitValue.getData());
            funcBody.addStatementOnExit(exstmtExit);
            funcBody.appendStatement(exstmtExit);
        } else if (onExitValue.getData() instanceof ArrayList<?>) {
            funcBody.addStatementsOnExit((ArrayList<Statement>) onExitValue.getData());
            funcBody.appendStatements((ArrayList<Statement>) onExitValue.getData());
        } else {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be either an expression or a statement list.");
            ((FunctionEnv) env).setReturnVal(retVal);
        }
    }

    private static Value isType(Value val, Value_t type) {
        if (val.getType().equals(type)) {
            return new StaticVal<>(Value_t.BOOLEAN, true);
        } else {
            return new StaticVal<>(Value_t.BOOLEAN, false);
        }
    }

    public static void isNull(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISNULL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.NULL);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isUndefined(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISUNDEFINED, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.UNDEFINED);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isInteger(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISINTEGER, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.INTEGER);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isReal(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISREAL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.REAL);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isString(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISSTRING, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.STRING);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isBoolean(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISBOOLEAN, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.BOOLEAN);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isTable(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISTABLE, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.TABLE);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isFunc(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISFUNC, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.USER_FUNCTION);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isLibFunc(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISLIBFUNC, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.LIBRARY_FUNCTION);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isObject(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISOBJECT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.OBJECT);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isAST(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISAST, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = isType(val, Value_t.AST);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void str(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.STR, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value ret = new StaticVal<>(Value_t.STRING, val.getData().toString());
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void sqrt(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SQRT, env)) {
            return;
        }

        Double data = getDoubleArgument(env);
        if (data == null) {
            return;
        }

        StaticVal retVal;
        data = Math.sqrt(data);

        //Return sprt
        if ((data == Math.floor(data)) && !Double.isInfinite(data)) {
            retVal = new StaticVal(Value_t.INTEGER, data);
        } else {
            retVal = new StaticVal(Value_t.REAL, data);
        }

        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void cos(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.COS, env)) {
            return;
        }

        Double data = getDoubleArgument(env);
        if (data == null) {
            return;
        }

        data = Math.cos(data);

        StaticVal retVal = new StaticVal(Value_t.REAL, data);
        ((FunctionEnv) env).setReturnVal(retVal);

    }

    public static void sin(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SIN, env)) {
            return;
        }

        Double data = getDoubleArgument(env);
        if (data == null) {
            return;
        }

        data = Math.sin(data);

        StaticVal retVal = new StaticVal(Value_t.REAL, data);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    private static boolean checkArgumentsNum(LibraryFunction_t func, Environment env) {
        if (func.equals(LibraryFunction_t.PRINT) || func.equals(LibraryFunction_t.PRINT_LN)) {
            return true;
        } else {
            if (env.totalActuals() != func.totalArgs()) {
                String msg = "Call to this lib function "
                        + "requires (" + func.totalArgs() + ") arguments BUT ("
                        + env.totalActuals() + ") found.";
                StaticVal retVal = new StaticVal(Value_t.ERROR, msg);
                ((FunctionEnv) env).setReturnVal(retVal);
                return false;
            } else {
                return true;
            }
        }
    }

    private static Double getDoubleArgument(Environment env) {
        double data;
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (value.isReal()) {
            data = (double) value.getData();

        } else if (value.isInteger()) {
            data = (double) (int) value.getData();

        } else if (value.isString() && value.isConvertedToNumeric()) {
            data = Double.parseDouble((String) value.getData());

        } else if (value.isUndefined()) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument is UNDEFINED.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;

        } else {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument can not cast to Double.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;

        }
        return data;
    }

    private static AFoxDataStructure getObjectArgument(Environment env) {
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!value.isObject() && !value.isTable()) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be of type Array or Object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;
        }
        AFoxDataStructure fdataStructure = (AFoxDataStructure) value.getData();
        return fdataStructure;
    }

    private static FunctionDef getFunctionArgument(Environment env) {
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!value.isUserFunction()) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be a user function.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;
        }
        FunctionDef funcdef = (FunctionDef) value.getData();
        return funcdef;
    }
}
