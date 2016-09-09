package libraryFunctions;

import utils.Constants;
import ast.ASTNode;
import ast.utils.ASTUtils;
import ast.ASTVisitor;
import ast.ASTVisitorException;
import java.util.HashMap;
import java.util.ArrayList;

import environment.Environment;
import environment.FunctionEnv;

import ast.Program;
import ast.Statement;
import ast.ExpressionStatement;
import ast.TermExpressionStmt;
import ast.Block;
import ast.Expression;
import ast.Lvalue;
import ast.IdentifierExpression;
import ast.AssignmentExpression;
import ast.BinaryExpression;
import ast.UnaryExpression;
import ast.MetaSyntax;
import ast.MetaExecute;
import ast.MetaEval;
import ast.MetaRun;
import ast.MetaToText;
import ast.Member;
import ast.FunctionDef;
import ast.BreakStatement;
import ast.ContinueStatement;
import ast.Call;
import ast.LvalueCall;
import ast.ExtendedCall;
import ast.AnonymousFunctionCall;
import ast.NormCall;
import ast.IndexedElement;
import ast.ObjectDefinition;
import ast.ArrayDef;
import ast.ReturnStatement;
import ast.IntegerLiteral;
import ast.DoubleLiteral;
import ast.StringLiteral;
import ast.NullLiteral;
import ast.TrueLiteral;
import ast.FalseLiteral;
import ast.IfStatement;
import ast.ForStatement;
import ast.WhileStatement;

import ast.visitors.ToStringASTVisitor;
import ast.visitors.IteratorASTVisitor;

import dataStructures.FoxObject;
import dataStructures.FoxArray;
import dataStructures.AFoxDataStructure;

import symbols.value.Value;
import symbols.value.Value_t;
import symbols.value.StaticVal;
import symbols.value.DynamicVal;

import static utils.Constants.LIBRARY_FUNC_ARG;

public class LibraryFunctions {

    public static void print(Environment env) throws ASTVisitorException {
        if (!checkArgumentsNum(LibraryFunction_t.PRINT, env)) {
            return;
        }

        int totalActuals = env.totalActuals();
        for (int i = 0; i < totalActuals; i++) {
            String data = "";

            Value argument = env.getActualArgument(LIBRARY_FUNC_ARG + i);
            if (argument.isUserFunction() || argument.isAST()) {
                ArrayList<Statement> stmtlist;
                if (!(argument.getData() instanceof ArrayList<?>)) {
                    stmtlist = new ArrayList<Statement>();
                    Statement exstmt = (argument.getData() instanceof Expression) ? 
                                          new ExpressionStatement((Expression) argument.getData())
                                        : (FunctionDef) argument.getData();
                    stmtlist.add(exstmt);
                } else {
                    stmtlist = (ArrayList<Statement>) argument.getData();
                }

                Program program = new Program(stmtlist);
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

    public static void factory(Environment env){
 
         if(env.totalActuals() % 2 !=0){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Call to factory lib function requires even number of arguments: "+env.totalActuals()+" found");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
          }
     
        int count = env.totalActuals();
        ArrayList<IndexedElement> indexElements = new ArrayList<IndexedElement>();
        ArrayList<IndexedElement> indexElements1 = new ArrayList<IndexedElement>();
        ObjectDefinition binAST = null;
        Value result = null;
       for(int i=0; i<count; i=i+2){
          int num2 = i+1;
             Value tempArg1 = env.getActualArgument(LIBRARY_FUNC_ARG+i);
             Value tempArg = env.getActualArgument(LIBRARY_FUNC_ARG+num2);
            indexElements.add(new IndexedElement((Expression)tempArg1.getData(),(Expression)tempArg.getData()));
            indexElements1.add(new IndexedElement((Expression)tempArg1.getData(),(Expression)tempArg.getData()));
        }
  
        ObjectDefinition ex = new ObjectDefinition(indexElements1);
          ReturnStatement returnStmt = new ReturnStatement(ex);
          ArrayList<Statement> stlist = new ArrayList<Statement>();
          stlist.add(returnStmt);
          Block bl = new Block(stlist);
          ArrayList<IdentifierExpression> idl = new ArrayList<IdentifierExpression>();
          indexElements.add(new IndexedElement(new StringLiteral("new"), new FunctionDef("#ANONYMOUS#_", idl, bl)));
     
          binAST = new ObjectDefinition(indexElements);
          result = new StaticVal<ASTNode>(Value_t.AST, binAST);
          ((FunctionEnv) env).setReturnVal(result);
    }

    public static void getAsObject(Environment env){
         if (!checkArgumentsNum(LibraryFunction_t.GETASOBJECT, env)) {
            return;
        }
        HashMap<Value, Value> objectData = new HashMap<Value, Value>();
        Value retVal = null;
        Statement body;

        Statement stmt = getStatementArgument(env);

        Value typeVal = null;
        // Create "type" field for object and get corresponding statement body
        Value typeName = new StaticVal(Value_t.STRING, "type");
        if(stmt instanceof IfStatement){
            body = ((IfStatement)stmt).getStatement();
            typeVal = new DynamicVal(new StaticVal(Value_t.STRING, "if"), "Object.(type)");

            // Set "expression" field for if statement object
            Value expressionName = new StaticVal(Value_t.STRING, "expression");
            Value staticExprVal = new StaticVal<ASTNode>(Value_t.AST, ((IfStatement)stmt).getExpression());
            Value expressionVal = new DynamicVal(staticExprVal, "expression");
            objectData.put(expressionName, expressionVal);
        }
        else if(stmt instanceof WhileStatement){
            body = ((WhileStatement)stmt).getStatement();
            typeVal = new DynamicVal(new StaticVal(Value_t.STRING, "while"), "Object.(type)");

            // Set "expression" field for while statement object
            Value expressionName = new StaticVal(Value_t.STRING, "expression");
            Value staticExprVal = new StaticVal<ASTNode>(Value_t.AST, ((WhileStatement)stmt).getExpression());
            Value expressionVal = new DynamicVal(staticExprVal, "expression");
            objectData.put(expressionName, expressionVal);
        }
        else if(stmt instanceof ForStatement){
            body = ((ForStatement)stmt).getStatement();
            typeVal = new DynamicVal(new StaticVal(Value_t.STRING, "for"), "Object.(type)");

            // Set "expression" field for for statement object
            Value expressionName = new StaticVal(Value_t.STRING, "expression");
            Value staticExprVal = new StaticVal<ASTNode>(Value_t.AST, ((ForStatement)stmt).getExpression());
            Value expressionVal = new DynamicVal(staticExprVal, "expression");
            objectData.put(expressionName, expressionVal);

            // Create "expressionlist1" field for function object
            ArrayList<Expression> expressions1 = ((ForStatement)stmt).getExpressionList1();
            HashMap<Value, Value> expressionData = new HashMap<Value, Value>();
            int count = 0;
            Value index;
            Value exprVal;
            Value expressionListName = new StaticVal(Value_t.STRING, "expressionlist1");
            for(Expression expr : expressions1){
                index = new StaticVal(Value_t.INTEGER, count);
                String errorInfo = "Object.("+count+")";
                staticExprVal = new StaticVal<ASTNode>(Value_t.AST, expr);
                exprVal = new DynamicVal(staticExprVal, errorInfo);
                expressionData.put(index, exprVal);
                count++;
            }
            FoxArray expressionArray = new FoxArray(expressionData);
            Value arrayVal = new StaticVal(Value_t.TABLE, expressionArray);

            Value indexedVal = new DynamicVal(arrayVal, "Object.(expressionlist1)");
            objectData.put(expressionListName, indexedVal);

            // Create "expressionlist2" field for function object
            ArrayList<Expression> expressions2 = ((ForStatement)stmt).getExpressionList2();
            expressionData = new HashMap<Value, Value>();
            count = 0;
            expressionListName = new StaticVal(Value_t.STRING, "expressionlist2");
            for(Expression expr : expressions2){
                index = new StaticVal(Value_t.INTEGER, count);
                String errorInfo = "Object.("+count+")";
                staticExprVal = new StaticVal<ASTNode>(Value_t.AST, expr);
                exprVal = new DynamicVal(staticExprVal, errorInfo);
                expressionData.put(index, exprVal);
                count++;
            }
            expressionArray = new FoxArray(expressionData);
            arrayVal = new StaticVal(Value_t.TABLE, expressionArray);

            indexedVal = new DynamicVal(arrayVal, "Object.(expressionlist1)");
            objectData.put(expressionListName, indexedVal);
        }
        else if(stmt instanceof FunctionDef){
            body = ((FunctionDef)stmt).getBody();
            typeVal = new DynamicVal(new StaticVal(Value_t.STRING, "function"), "Object.(type)");

            // Create "name" field for function object
            String funcName = ((FunctionDef)stmt).getFuncName();
            Value nameVal = new DynamicVal(new StaticVal(Value_t.STRING, funcName), "Object.(name)");
            Value nameName = new StaticVal(Value_t.STRING, "name");
            objectData.put(nameName, nameVal);

            // Create "arguments" field for function object
            ArrayList<IdentifierExpression> arguments = ((FunctionDef)stmt).getArguments();
            HashMap<Value, Value> argumentData = new HashMap<Value, Value>();
            int count = 0;
            Value index;
            Value staticargVal;
            Value argVal;
            Value argumentsName = new StaticVal(Value_t.STRING, "arguments");
            for(IdentifierExpression id : arguments){
                index = new StaticVal(Value_t.INTEGER, count);
                String errorInfo = "Object.("+count+")";
                staticargVal = new StaticVal<String>(Value_t.STRING, id.getIdentifier());
                argVal = new DynamicVal(staticargVal, errorInfo);
                argumentData.put(index, argVal);
                count++;
            }
            FoxArray argumentArray = new FoxArray(argumentData);
            Value arrayVal = new StaticVal(Value_t.TABLE, argumentArray);

            Value indexedVal = new DynamicVal(arrayVal, "Object.(arguments)");
            objectData.put(argumentsName, indexedVal);
        }
        else{
            String msg = "Argument of getAsObject() should be a Statement AST";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        objectData.put(typeName, typeVal);


        // Create "statements" field for object and populate it accordingly
        HashMap<Value, Value> statementData = new HashMap<Value, Value>();
        int count = 0;
        Value index;
        Value value;
        ArrayList<Statement> stmtlist;
        if(body instanceof Block){
            stmtlist = ((Block) body).getStatementList();
        }else{
            stmtlist = new ArrayList<Statement>();
            stmtlist.add(body);
        }

        for(Statement cur_stmt : stmtlist){
            index = new StaticVal(Value_t.INTEGER, count);
            String errorInfo = "Object.(" + count + ")";
            // If a compound statement is found, call this method recursively
            if(cur_stmt instanceof IfStatement || cur_stmt instanceof WhileStatement || cur_stmt instanceof ForStatement){
                FunctionEnv tmpEnv = new FunctionEnv();
                StaticVal staticval = new StaticVal(Value_t.AST, cur_stmt);
                tmpEnv.insert(LIBRARY_FUNC_ARG + 0, new DynamicVal(staticval, errorInfo));
                getAsObject(tmpEnv);
                value = ((FunctionEnv) tmpEnv).getReturnVal();
                if (tmpEnv.getReturnVal().getType().equals(Value_t.ERROR)) {
                    return;
                }
            }else{
                value = new StaticVal<ASTNode>(Value_t.AST, cur_stmt);
            }
            Value element = new DynamicVal(value, errorInfo);

            statementData.put(index, element);
            count++;
        }
        FoxArray farray = new FoxArray(statementData);
        Value arrayVal = new StaticVal(Value_t.TABLE, farray);

        
        // Set object's statement list
        Value indexedVal = new DynamicVal(arrayVal, "Object.(statements)");
        Value indexedName = new StaticVal(Value_t.STRING, "statements");
        objectData.put(indexedName, indexedVal);
        
        // Construct final FoxObject and return
        FoxObject fobject = new FoxObject(objectData);
        retVal = new StaticVal(Value_t.OBJECT, fobject);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void iterator(Environment env) throws ASTVisitorException{
        // Check for # of arguments here
        FunctionDef funcdef = getFunctionArgument(env);
        if (funcdef == null) {
            return;
        }
        
        ASTVisitor astVisitor = new IteratorASTVisitor();
        funcdef.accept(astVisitor);

        HashMap<Value, Value> objectData = new HashMap<Value, Value>();
        // Set "iter" field
        Value iterName = new StaticVal<String>(Value_t.STRING, "iter");
        Value iterValue = new StaticVal(Value_t.OBJECT, astVisitor);
        iterValue = new DynamicVal(iterValue, "iter");
        objectData.put(iterName, iterValue);

        // Compose "next" field, to call getNext on iterator
        Value nextName = new StaticVal<String>(Value_t.STRING, "next");
        //Argument list consists only of the given iterator
        ArrayList<IdentifierExpression> arguments = new ArrayList<IdentifierExpression>();
        arguments.add(new IdentifierExpression("iterator"));
        // Function body is an LvalueCall to the library function `getNext()`
        Lvalue lvalue = new IdentifierExpression("getNextItem");
        // Argument to `getNext` LvalueCall is the "iter" field of the given iterator
        Lvalue memberLvalue = new IdentifierExpression("iterator");
        String memberIdentifier = "iter";
        Member member = new Member(memberLvalue, memberIdentifier, null, null);
        // Set LvalueCall's call suffix
        ArrayList<Expression> actualArguments = new ArrayList<Expression>();
        actualArguments.add(member);
        NormCall callsuffix = new NormCall(actualArguments);
        LvalueCall getNextCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        ReturnStatement retstmt = new ReturnStatement(getNextCall);
        // The above forms an ExpressionStatement of the form ==>  getNext(iterator.iter);
        ArrayList<Statement> stmtlist = new ArrayList<Statement>();
        stmtlist.add(retstmt);
        Block funcBody = new Block(stmtlist);

        // Set final "next" function field
        FunctionDef nextFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value nextValue = new StaticVal(Value_t.USER_FUNCTION, nextFuncDef);
        nextValue = new DynamicVal(nextValue, "next");
        objectData.put(nextName, nextValue);

        // Compose "prev" field, to call getPrevItem on iterator
        Value prevName = new StaticVal<String>(Value_t.STRING, "prev");
        // Compose LvalueCall for getPrevItem
        lvalue = new IdentifierExpression("getPrevItem");
        // Use same call suffix as above
        LvalueCall getPrevItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        retstmt = new ReturnStatement(getPrevItemCall);
        stmtlist = new ArrayList<Statement>();
        stmtlist.add(retstmt);
        funcBody = new Block(stmtlist);
        FunctionDef prevFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value prevValue = new StaticVal(Value_t.USER_FUNCTION, prevFuncDef);
        prevValue = new DynamicVal(prevValue, "prev");
        objectData.put(prevName, prevValue);

        // Compose "hasNext" field, to call hasNextItem on iterator
        Value hasNextName = new StaticVal<String>(Value_t.STRING, "hasNext");
        // Compose LvalueCall for hasNextItem
        lvalue = new IdentifierExpression("hasNextItem");
        // Use same call suffix as above
        LvalueCall hasNextItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        retstmt = new ReturnStatement(hasNextItemCall);
        stmtlist = new ArrayList<Statement>();
        stmtlist.add(retstmt);
        funcBody = new Block(stmtlist);
        FunctionDef hasNextFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value hasNextValue = new StaticVal(Value_t.USER_FUNCTION, hasNextFuncDef);
        hasNextValue = new DynamicVal(hasNextValue, "hasNext");
        objectData.put(hasNextName, hasNextValue);

        // Compose "hasPrev" field, to call hasNextItem on iterator
        Value hasPrevName = new StaticVal<String>(Value_t.STRING, "hasPrev");
        // Compose LvalueCall for hasNextItem
        lvalue = new IdentifierExpression("hasPrevItem");
        // Use same call suffix as above
        LvalueCall hasPrevItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        retstmt = new ReturnStatement(hasPrevItemCall);
        stmtlist = new ArrayList<Statement>();
        stmtlist.add(retstmt);
        funcBody = new Block(stmtlist);
        FunctionDef hasPrevFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value hasPrevValue = new StaticVal(Value_t.USER_FUNCTION, hasPrevFuncDef);
        hasPrevValue = new DynamicVal(hasPrevValue, "hasNext");
        objectData.put(hasPrevName, hasPrevValue);


        FoxObject fobject = new FoxObject(objectData);
        Value retVal = new StaticVal(Value_t.OBJECT, fobject);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getNextItem(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.GETNEXTITEM, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if(!(val.getData() instanceof IteratorASTVisitor)){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument to getNext should be an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) val.getData();

        int curItem = astVisitor.getCurItem();
        if(curItem >= astVisitor.getStatementList().size()){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Statement iterator out of bounds.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        // Statement curStmt = astVisitor.getStatement(curItem);
        Statement curStmt = astVisitor.getNextStatement();
        Value retVal;
        Expression expr = null;
        if(curStmt instanceof ExpressionStatement){  // When an ExpressionStatement is next, return its expression instead
            expr = ((ExpressionStatement)curStmt).getExpression();
            retVal = new StaticVal(Value_t.AST, expr);
        } else{
            retVal = new StaticVal(Value_t.AST, curStmt);
        }
        
        // Increment curItem counter
        // astVisitor.incCurItem();
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getPrevItem(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.GETPREVITEM, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if(!(val.getData() instanceof IteratorASTVisitor)){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument to getNext should be an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) val.getData();

        int curItem = astVisitor.getCurItem();
        if(curItem <= 0){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Statement iterator out of bounds.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        // Statement curStmt = astVisitor.getStatement(curItem);
        Statement curStmt = astVisitor.getPrevStatement();
        Value retVal;
        Expression expr = null;
        if(curStmt instanceof ExpressionStatement){  // When an ExpressionStatement is next, return its expression instead
            expr = ((ExpressionStatement)curStmt).getExpression();
            retVal = new StaticVal(Value_t.AST, expr);
        } else{
            retVal = new StaticVal(Value_t.AST, curStmt);
        }
        
        // Increment curItem counter
        // astVisitor.decCurItem();
        ((FunctionEnv) env).setReturnVal(retVal);
    }


    public static void hasNextItem(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.HASNEXTITEM, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if(!(val.getData() instanceof IteratorASTVisitor)){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument to getNext should be an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) val.getData();
        int curItem = astVisitor.getCurItem();
        StaticVal retVal;
        if(curItem >= astVisitor.getStatementList().size()-1){
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.FALSE);
        }else{
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.TRUE);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void hasPrevItem(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.HASPREVITEM, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if(!(val.getData() instanceof IteratorASTVisitor)){
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument to getNext should be an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) val.getData();
        int curItem = astVisitor.getCurItem();
        StaticVal retVal;
        if(curItem <= 0){
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.FALSE);
        }else{
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.TRUE);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    // AST type checking
    private static Value isASTType(Value val, String className) {
            String checkClass = "class ast."+className;
            String originClass = val.getData().getClass().toString();
            if(originClass.equals(checkClass)){
                return new StaticVal<>(Value_t.BOOLEAN, true);
            }else{
                return new StaticVal<>(Value_t.BOOLEAN, false);
            }
    }

    public static void isStatement(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISSTATEMENT, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        // instanceof is needed here because all statements are subclasses of Statement
        Value retVal = (val.getData() instanceof Statement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }    

    public static void isIfStatement(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISIFSTATEMENT, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof IfStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isForStatement(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISFORSTATEMENT, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ForStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isWhileStatement(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISWHILESTATEMENT, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof WhileStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isReturnStatement(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISRETURNSTATEMENT, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ReturnStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isBreakStatement(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISBREAKSTATEMENT, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof BreakStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isContinueStatement(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISCONTINUESTATEMENT, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ContinueStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isFunctionDefinition(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISFUNCTIONDEFINITION, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof FunctionDef) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isExpression(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISEXPRESSION, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof Expression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }    

    public static void isAssignmentExpression(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISASSIGNMENTEXPRESSION, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof AssignmentExpression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isBinaryExpression(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISBINARYEXPRESSION, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof BinaryExpression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isUnaryExpression(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISUNARYEXPRESSION, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof UnaryExpression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaSyntax(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISMETASYNTAX, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaSyntax) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaExecute(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISMETAEXECUTE, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaExecute) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaRun(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISMETARUN, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaRun) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }    

    public static void isMetaEval(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISMETAEVAL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaEval) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaToText(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISMETATOTEXT, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaToText) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isIdentifier(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISIDENTIFIER, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof IdentifierExpression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMember(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISMEMBER, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof Member) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isCall(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISCALL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof Call) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isLvalueCall(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISLVALUECALL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof LvalueCall) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isExtendedCall(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISEXTENDEDCALL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ExtendedCall) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isAnonymousCall(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISANONYMOUSCALL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof AnonymousFunctionCall) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isObjectDefinition(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISOBJECTDEFINITION, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ObjectDefinition) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isArrayDefinition(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISARRAYDEFINITION, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ArrayDef) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isIntegerLiteral(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISINTEGERLITERAL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof IntegerLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isDoubleLiteral(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISDOUBLELITERAL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof DoubleLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isStringLiteral(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISSTRINGLITERAL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof StringLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isNullLiteral(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISNULLLITERAL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof NullLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isTrueLiteral(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISTRUELITERAL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof TrueLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isFalseLiteral(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISFALSELITERAL, env)){
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof FalseLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    // ASTNode's getters for fox
    public static void getExpression(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.GETEXPRESSION, env)){
            return;
        }
        Value retVal = utils.Constants.NULL;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if(!val.getType().equals(Value_t.AST)){
            retVal = new StaticVal(Value_t.ERROR, "getExpression requires an AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        ASTNode ast = (ASTNode) val.getData();
        if(ast instanceof IfStatement)
            retVal = new StaticVal(Value_t.AST, ((IfStatement) ast).getExpression());
        else if(ast instanceof ForStatement)
            retVal = new StaticVal(Value_t.AST, ((ForStatement) ast).getExpression());
        else if(ast instanceof WhileStatement)
            retVal = new StaticVal(Value_t.AST, ((WhileStatement) ast).getExpression());
        else if(ast instanceof ReturnStatement)
            retVal = new StaticVal(Value_t.AST, ((ReturnStatement) ast).getExpression());
        else if(ast instanceof AssignmentExpression)
            retVal = new StaticVal(Value_t.AST, ((AssignmentExpression) ast).getExpression());
        else if(ast instanceof UnaryExpression)
            retVal = new StaticVal(Value_t.AST, ((UnaryExpression) ast).getExpression());
        else if(ast instanceof MetaSyntax)
            retVal = new StaticVal(Value_t.AST, ((MetaSyntax) ast).getExpression());
        else if(ast instanceof MetaExecute)
            retVal = new StaticVal(Value_t.AST, ((MetaExecute) ast).getExpression());
        else if(ast instanceof MetaRun)
            retVal = new StaticVal(Value_t.AST, ((MetaRun) ast).getExpression());
        else if(ast instanceof MetaEval)
            retVal = new StaticVal(Value_t.AST, ((MetaEval) ast).getExpression());
        else if(ast instanceof MetaToText)
            retVal = new StaticVal(Value_t.AST, ((MetaToText) ast).getExpression());
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void setExpression(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.SETEXPRESSION, env)){
            return;
        }
        Value retVal = utils.Constants.NULL;
        Value stmtVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value exprVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if(!stmtVal.getType().equals(Value_t.AST) || !exprVal.getType().equals(Value_t.AST) || !(stmtVal.getData() instanceof Statement)){
            retVal = new StaticVal(Value_t.ERROR, "setExpression requires a statement AST and an expression AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        ASTNode stmtast = (ASTNode) stmtVal.getData();
        if(!(exprVal.getData() instanceof Expression)){
            retVal = new StaticVal(Value_t.ERROR, "setExpression's second argument should be an expression AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        Expression expr = (Expression) exprVal.getData();

        if(stmtast instanceof IfStatement)
            ((IfStatement) stmtast).setExpression(expr);
        else if(stmtast instanceof ForStatement)
            ((ForStatement) stmtast).setExpression(expr);
        else if(stmtast instanceof WhileStatement)
            ((WhileStatement) stmtast).setExpression(expr);
        else if(stmtast instanceof ReturnStatement)
            ((ReturnStatement) stmtast).setExpression(expr);
        else if(stmtast instanceof AssignmentExpression)
            ((AssignmentExpression) stmtast).setExpression(expr);
        else if(stmtast instanceof UnaryExpression)
            ((UnaryExpression) stmtast).setExpression(expr);
        else if(stmtast instanceof MetaSyntax)
            ((MetaSyntax) stmtast).setExpression(expr);
        else if(stmtast instanceof MetaExecute)
            ((MetaExecute) stmtast).setExpression(expr);
        else if(stmtast instanceof MetaRun)
            ((MetaRun) stmtast).setExpression(expr);
        else if(stmtast instanceof MetaEval)
            ((MetaEval) stmtast).setExpression(expr);
        else if(stmtast instanceof MetaToText)
            ((MetaToText) stmtast).setExpression(expr);
        else{
            retVal = new StaticVal(Value_t.ERROR, "setExpression's first argument should be a statement AST that "
                                                    +"has an expression");
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    // Fox type checking
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

    public static void isArray(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISARRAY, env)) {
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

    private static Statement getStatementArgument(Environment env){
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if(!(value.getData() instanceof Statement)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be a statement AST.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;
        }
        Statement stmt = (Statement) value.getData();
        return stmt;
    }

}
