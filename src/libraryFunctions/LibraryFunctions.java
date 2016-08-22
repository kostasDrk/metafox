package libraryFunctions;


import java.util.ArrayList;

import environment.Environment;
import environment.FunctionEnv;

import ast.Statement;
import ast.ExpressionStatement;
import ast.Block;
import ast.ReturnStatement;
import ast.Expression;
import ast.FunctionDef;
import ast.NormCall;
import ast.LvalueCall;
import ast.IdentifierExpression;
import ast.StringLiteral;


import dataStructures.FoxObject;
import dataStructures.FoxArray;
import dataStructures.FoxDataStructure;

import symbols.value.Value;
import symbols.value.Value_t;
import symbols.value.DynamicVal;
import symbols.value.StaticVal;

import static utils.Constants.LIBRARY_FUNC_ARG;

public class LibraryFunctions {

    public static void print(Environment env) {
        int totalActuals = env.totalActuals();

        for (int i = 0; i < totalActuals; i++) {
            String data;

            if (env.getActualArgument(LIBRARY_FUNC_ARG + i).isUndefined()) {
                data = "UNDEFINED";
            } else if (env.getActualArgument(LIBRARY_FUNC_ARG + i).isNull()) {
                data = "NULL";
            } else {
                data = env.getActualArgument(LIBRARY_FUNC_ARG + i).getData().toString();
            }

            System.out.print(data);
        }
    }

    public static void println(Environment env) {
        print(env);
        System.out.println("");
    }
    public static void len(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        FoxDataStructure fdataStructure = getObjectArgument(env);
        if(fdataStructure == null){
            return;
        }
        StaticVal retVal = new StaticVal(Value_t.INTEGER, fdataStructure.size());
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void keys(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        FoxDataStructure fobject = getObjectArgument(env);
        if(!(fobject instanceof FoxObject)){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be of type Object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        FoxArray farray = new FoxArray(((FoxObject)fobject).keys());
        
        StaticVal retVal = new StaticVal(Value_t.TABLE, farray);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void values(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        FoxDataStructure fdataStructure = getObjectArgument(env);
        if(fdataStructure == null){
            return;
        }
        FoxArray farray = new FoxArray(fdataStructure.values());
        StaticVal retVal = new StaticVal(Value_t.TABLE, farray);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void diagnose(Environment env){
        if(!checkArgumentNum(env, 3)) return;
        DynamicVal tempArg = env.popArgument(LIBRARY_FUNC_ARG+2);
        addFirst(env);
        env.insert(LIBRARY_FUNC_ARG+1, tempArg);
        //addLast(env);
        addOnExitPoints(env);
    }

    public static void addFirst(Environment env){
        if(!checkArgumentNum(env, 2)) return;
        FunctionDef funcdef = getFunctionArgument(env);
        if(funcdef == null) return;
        Value onEnterValue = env.getActualArgument(LIBRARY_FUNC_ARG+1);
        Block funcBody = funcdef.getBody();

        if(onEnterValue.getData() instanceof Expression){
            ExpressionStatement exstmtEnter = new ExpressionStatement((Expression)onEnterValue.getData());
            funcBody.prependStatement(exstmtEnter);
        }else if (onEnterValue.getData() instanceof ArrayList<?>){
            funcBody.prependStatements((ArrayList<Statement>) onEnterValue.getData());
        }else{
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be either an expression or a statement list.");
            ((FunctionEnv) env).setReturnVal(retVal);
        }
    }

    public static void addLast(Environment env){
        if(!checkArgumentNum(env, 2)) return;
        FunctionDef funcdef = getFunctionArgument(env);
        if(funcdef == null) return;
        Value onExitValue = env.getActualArgument(LIBRARY_FUNC_ARG+1);
        Block funcBody = funcdef.getBody();

        if(onExitValue.getData() instanceof Expression){
            ExpressionStatement exstmtExit = new ExpressionStatement((Expression) onExitValue.getData());
            funcBody.appendStatement(exstmtExit);
        }else if (onExitValue.getData() instanceof ArrayList<?>){
            funcBody.appendStatements((ArrayList<Statement>) onExitValue.getData());
        }else{
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be either an expression or a statement list.");
            ((FunctionEnv) env).setReturnVal(retVal);
        }
    }

    public static void addOnExitPoints(Environment env){
        if(!checkArgumentNum(env, 2)) return;
        FunctionDef funcdef = getFunctionArgument(env);
        if(funcdef == null) return;
        Value onExitValue = env.getActualArgument(LIBRARY_FUNC_ARG+1);
        Block funcBody = funcdef.getBody();

        if(onExitValue.getData() instanceof Expression){
            ExpressionStatement exstmtExit = new ExpressionStatement((Expression) onExitValue.getData());
            funcBody.addStatementOnExit(exstmtExit);
        }else if (onExitValue.getData() instanceof ArrayList<?>){
            funcBody.appendStatements((ArrayList<Statement>) onExitValue.getData());
        }else{
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be either an expression or a statement list.");
            ((FunctionEnv) env).setReturnVal(retVal);
        }
    }

    private static Value isType(Value val, Value_t type){
        if(val.getType().equals(type)){
            return new StaticVal<Boolean>(Value_t.BOOLEAN, true);
        }else{
            return new StaticVal<Boolean>(Value_t.BOOLEAN, false);
        }
    }

    public static void isNull(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.NULL);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isUndefined(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.UNDEFINED);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isInteger(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.INTEGER);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isReal(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.REAL);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isString(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.STRING);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isBoolean(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.BOOLEAN);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isTable(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.TABLE);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isFunc(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.USER_FUNCTION);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isLibFunc(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.LIBRARY_FUNCTION);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isObject(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.OBJECT);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void isAST(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = isType(val, Value_t.AST);
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void str(Environment env){
        if(!checkArgumentNum(env, 1)) return;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        Value ret = new StaticVal<String>(Value_t.STRING, val.getData().toString());
        ((FunctionEnv) env).setReturnVal(ret);
    }

    public static void sqrt(Environment env) {
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

        Double data = getDoubleArgument(env);
        if (data == null) {
            return;
        }

        data = Math.cos(data);

        StaticVal retVal = new StaticVal(Value_t.REAL, data);
        ((FunctionEnv) env).setReturnVal(retVal);

    }

    public static void sin(Environment env) {

        Double data = getDoubleArgument(env);
        if (data == null) {
            return;
        }

        data = Math.sin(data);

        StaticVal retVal = new StaticVal(Value_t.REAL, data);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    private static boolean checkArgumentNum(Environment env, int args){
        if(env.totalActuals() != args){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Call to lib function requires "+args+" arguments: "+env.totalActuals()+" found");
            ((FunctionEnv) env).setReturnVal(retVal);
            return false;
        }
        return true;
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

    private static FoxDataStructure getObjectArgument(Environment env){
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        if(!value.isObject() && !value.isTable()){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be of type Array or Object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;
        }
        FoxDataStructure fdataStructure = (FoxDataStructure)value.getData();
        return fdataStructure;
    }

    private static FunctionDef getFunctionArgument(Environment env){
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG+0);
        if(!value.isUserFunction()){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be a user function.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;
        }
        FunctionDef funcdef = (FunctionDef) value.getData();
        return funcdef;
    }
}
