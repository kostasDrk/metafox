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

import symbols.value.StaticVal;
import symbols.value.Value;
import symbols.value.Value_t;

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
        FoxDataStructure fdataStructure = getObjectArgument(env);
        if(fdataStructure == null){
            return;
        }
        StaticVal retVal = new StaticVal(Value_t.INTEGER, fdataStructure.size());
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void keys(Environment env){
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
        FoxDataStructure fdataStructure = getObjectArgument(env);
        if(fdataStructure == null){
            return;
        }
        FoxArray farray = new FoxArray(fdataStructure.values());
        StaticVal retVal = new StaticVal(Value_t.TABLE, farray);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void diagnose(Environment env){
        FunctionDef funcdef = getFunctionArgument(env);
        if(funcdef == null) return;
        Value onEnterValue = env.getActualArgument(LIBRARY_FUNC_ARG+1);
        String onEnter = (onEnterValue.isUndefined() || onEnterValue.isNull()) ? "" : onEnterValue.getData().toString();
        Value onExitValue = env.getActualArgument(LIBRARY_FUNC_ARG+2);
        String onExit = (onExitValue.isUndefined() || onExitValue.isNull()) ? "" : onExitValue.getData().toString();
        
        Block funcBody = funcdef.getBody();
        ArrayList<Statement> stmtlist = funcBody.getStatementList();
        int size = stmtlist.size();

        // Prepare new statements to add to function body
        IdentifierExpression printid = new IdentifierExpression(LibraryFunction_t.PRINT_LN.toString(), false);
        ArrayList<Expression> elist = new ArrayList<Expression>();
        elist.add(new StringLiteral(onEnter));
        NormCall normcall = new NormCall(elist);
        LvalueCall lcall = new LvalueCall(printid, normcall);
        ExpressionStatement exstmt = new ExpressionStatement(lcall);
        funcBody.prependStatement(exstmt);

        elist = new ArrayList<Expression>();
        elist.add(new StringLiteral(onExit));
        normcall = new NormCall(elist);
        lcall = new LvalueCall(printid, normcall);
        exstmt = new ExpressionStatement(lcall);
        if(stmtlist.get(size) instanceof ReturnStatement)
            funcBody.addStatement(exstmt, size);
        else
            funcBody.appendStatement(exstmt);
    }

    public static void str(Environment env){
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
