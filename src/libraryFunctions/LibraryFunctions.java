package libraryFunctions;

import ast.*;
import ast.visitors.ToStringASTVisitor;
import ast.visitors.IteratorASTVisitor;

import dataStructures.*;

import environment.Environment;
import environment.FunctionEnv;

import symbols.value.*;
import symbols.utils.Symbol;

import static utils.Constants.LIBRARY_FUNC_ARG;
import static utils.Constants.NULL;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;

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
                ArrayList<Statement> stmtlist;
                if (!(argument.getData() instanceof ArrayList<?>)) {
                    stmtlist = new ArrayList<>();
                    Statement exstmt = (argument.getData() instanceof Expression)
                            ? new ExpressionStatement((Expression) argument.getData())
                            : (Statement) argument.getData();
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

    public static void isEmpty(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.ISEMPTY, env)) {
            return;
        }

        AFoxDataStructure fobject = getObjectArgument(env);
        if (!(fobject instanceof FoxArray)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "First argument must be of type Array.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        Value retVal;
        FoxArray farray = (FoxArray) fobject;
        if(fobject.size() > 0)
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.FALSE);
        else
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.TRUE);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void push(Environment env){
        if (!checkArgumentsNum(LibraryFunction_t.PUSH, env)) {
            return;
        }
        AFoxDataStructure fobject = getObjectArgument(env);
        if (!(fobject instanceof FoxArray)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "First argument must be of type Array.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        Value newVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        FoxArray farray = (FoxArray) fobject;
        farray.add(newVal);
    }

    public static void copy(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.COPY, env)) {
            return;
        }

        AFoxDataStructure fdataStructure = getObjectArgument(env);
        if (fdataStructure == null) {
            return;
        }

        StaticVal retVal = null;

        if (fdataStructure instanceof FoxObject) {
            retVal = new StaticVal(Value_t.OBJECT, new FoxObject((FoxObject) fdataStructure));

        } else if (fdataStructure instanceof FoxArray) {
            retVal = new StaticVal(Value_t.TABLE, new FoxArray((FoxArray) fdataStructure));

        } else {
            //fatal error
        }

        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void contains(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.CONTAINS, env)) {
            return;
        }

        Value retVal;
        Value stringVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value subStringVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if (!(stringVal.getData() instanceof String)
                || !(subStringVal.getData() instanceof String)) {

            String msg = "Requires two Strings.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        boolean contained = ((String) stringVal.getData()).contains((String) subStringVal.getData());
        retVal = new StaticVal(Value_t.BOOLEAN, contained);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    private static Symbol toSymbol(String id) {
        return new Symbol(new IdentifierExpression(id));
    }

    public static void iterator(Environment env) throws ASTVisitorException {
        if (!checkArgumentsNum(LibraryFunction_t.ITERATOR, env)) {
            return;
        }

        // FunctionDef funcdef = getFunctionArgument(env);
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!value.getType().equals(Value_t.AST) && !value.getType().equals(Value_t.USER_FUNCTION)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "iterator requires a statement AST.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        ASTNode astNode;
        if (value.getData() instanceof ArrayList<?>) {
            astNode = new Block((ArrayList<Statement>) value.getData());
        } else if (value.getData() instanceof Statement) {
            astNode = (Statement) value.getData();
        } else if (value.getData() instanceof Program) {
            astNode = (Program) value.getData();
            ArrayList<Statement> stmtlist = (ArrayList) ((Program) astNode).getStatements();
            astNode = new Block(stmtlist);
        } else {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "iterator requires a statement AST.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        ASTVisitor astVisitor;
        astVisitor = new IteratorASTVisitor();
        ((Statement) astNode).accept(astVisitor);

        HashMap<Value, Value> objectData = new HashMap<>();
        // Set "iter" field
        Value iterName = new StaticVal<>(Value_t.STRING, "iter");
        Value iterValue = new DynamicVal(Value_t.OBJECT, astVisitor, "iter");
        objectData.put(iterName, iterValue);

        // Compose "next" field, to call getNext on iterator
        Value nextName = new StaticVal<>(Value_t.STRING, "next");
        //Argument list consists only of the given iterator
        ArrayList<IdentifierExpression> arguments = new ArrayList<>();
        arguments.add(new IdentifierExpression("iterator"));
        // Function body is an LvalueCall to the library function `getNext()`
        Lvalue lvalue = new IdentifierExpression("getNextItem");
        // Argument to `getNext` LvalueCall is the "iter" field of the given iterator
        Lvalue memberLvalue = new IdentifierExpression("iterator");
        IdentifierExpression memberIdentifier = new IdentifierExpression("iter");
        Member member = new Member(memberLvalue, memberIdentifier, null, null);
        // Set LvalueCall's call suffix
        ArrayList<Expression> actualArguments = new ArrayList<>();
        actualArguments.add(member);
        NormCall callsuffix = new NormCall(actualArguments);
        LvalueCall getNextCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        ReturnStatement retstmt = new ReturnStatement(getNextCall);
        // The above forms an ExpressionStatement of the form ==>  getNext(iterator.iter);
        ArrayList<Statement> stmtlist = new ArrayList<>();
        stmtlist.add(retstmt);
        Block funcBody = new Block(stmtlist);

        // Set final "next" function field
        FunctionDef nextFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value nextValue = new DynamicVal(Value_t.USER_FUNCTION, nextFuncDef, "next");
        objectData.put(nextName, nextValue);

        // Compose "prev" field, to call getPrevItem on iterator
        Value prevName = new StaticVal<>(Value_t.STRING, "prev");
        // Compose LvalueCall for getPrevItem
        lvalue = new IdentifierExpression("getPrevItem");
        // Use same call suffix as above
        LvalueCall getPrevItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        retstmt = new ReturnStatement(getPrevItemCall);
        stmtlist = new ArrayList<>();
        stmtlist.add(retstmt);
        funcBody = new Block(stmtlist);
        FunctionDef prevFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value prevValue = new DynamicVal(Value_t.USER_FUNCTION, prevFuncDef, "prev");
        objectData.put(prevName, prevValue);

        // Compose "hasNext" field, to call hasNextItem on iterator
        Value hasNextName = new StaticVal<>(Value_t.STRING, "hasNext");
        // Compose LvalueCall for hasNextItem
        lvalue = new IdentifierExpression("hasNextItem");
        // Use same call suffix as above
        LvalueCall hasNextItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        retstmt = new ReturnStatement(hasNextItemCall);
        stmtlist = new ArrayList<>();
        stmtlist.add(retstmt);
        funcBody = new Block(stmtlist);
        FunctionDef hasNextFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value hasNextValue = new StaticVal(Value_t.USER_FUNCTION, hasNextFuncDef);
        hasNextValue = new DynamicVal(hasNextValue, "hasNext");
        objectData.put(hasNextName, hasNextValue);

        // Compose "hasPrev" field, to call hasNextItem on iterator
        Value hasPrevName = new StaticVal<>(Value_t.STRING, "hasPrev");
        // Compose LvalueCall for hasNextItem
        lvalue = new IdentifierExpression("hasPrevItem");
        // Use same call suffix as above
        LvalueCall hasPrevItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        retstmt = new ReturnStatement(hasPrevItemCall);
        stmtlist = new ArrayList<>();
        stmtlist.add(retstmt);
        funcBody = new Block(stmtlist);
        FunctionDef hasPrevFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value hasPrevValue = new StaticVal(Value_t.USER_FUNCTION, hasPrevFuncDef);
        hasPrevValue = new DynamicVal(hasPrevValue, "hasNext");
        objectData.put(hasPrevName, hasPrevValue);

        // Compose "remove" field, to call hasNextItem on iterator
        Value removeName = new StaticVal<>(Value_t.STRING, "remove");
        // Compose LvalueCall for hasNextItem
        lvalue = new IdentifierExpression("removeItem");
        LvalueCall removeItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        ExpressionStatement exprstmt = new ExpressionStatement(removeItemCall);
        stmtlist = new ArrayList<>();
        stmtlist.add(exprstmt);
        funcBody = new Block(stmtlist);
        FunctionDef removeFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value removeValue = new StaticVal(Value_t.USER_FUNCTION, removeFuncDef);
        removeValue = new DynamicVal(removeValue, "remove");
        objectData.put(removeName, removeValue);

        // Compose "addBefore" field, to call hasNextItem on iterator
        Value addName = new StaticVal<>(Value_t.STRING, "addBefore");
        arguments = new ArrayList<>();
        arguments.add(new IdentifierExpression("iterator"));
        arguments.add(new IdentifierExpression("newStmt"));
        // Compose LvalueCall for hasNextItem
        lvalue = new IdentifierExpression("addItemBefore");
        actualArguments = new ArrayList<>();
        actualArguments.add(member);
        actualArguments.add(new IdentifierExpression("newStmt"));
        callsuffix = new NormCall(actualArguments);
        LvalueCall addItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        exprstmt = new ExpressionStatement(addItemCall);
        stmtlist = new ArrayList<>();
        stmtlist.add(exprstmt);
        funcBody = new Block(stmtlist);
        FunctionDef addFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value addValue = new StaticVal(Value_t.USER_FUNCTION, addFuncDef);
        addValue = new DynamicVal(addValue, "addBefore");
        objectData.put(addName, addValue);

        // Compose "addAfter" field, to call hasNextItem on iterator
        addName = new StaticVal<>(Value_t.STRING, "addAfter");
        // Compose LvalueCall for hasNextItem
        lvalue = new IdentifierExpression("addItemAfter");
        addItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        exprstmt = new ExpressionStatement(addItemCall);
        stmtlist = new ArrayList<>();
        stmtlist.add(exprstmt);
        funcBody = new Block(stmtlist);
        addFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        addValue = new StaticVal(Value_t.USER_FUNCTION, addFuncDef);
        addValue = new DynamicVal(addValue, "addAfter");
        objectData.put(addName, addValue);

        // Compose "replace" field, to call hasNextItem on iterator
        Value replaceName = new StaticVal<>(Value_t.STRING, "replace");
        // Compose LvalueCall for hasNextItem
        lvalue = new IdentifierExpression("replaceItem");
        LvalueCall replaceItemCall = new LvalueCall(lvalue, callsuffix);
        // Create the corresponding expression statement
        exprstmt = new ExpressionStatement(replaceItemCall);
        stmtlist = new ArrayList<>();
        stmtlist.add(exprstmt);
        funcBody = new Block(stmtlist);
        FunctionDef replaceFuncDef = new FunctionDef("#ANONYMOUS#_", arguments, funcBody);
        Value replaceValue = new StaticVal(Value_t.USER_FUNCTION, replaceFuncDef);
        replaceValue = new DynamicVal(replaceValue, "replace");
        objectData.put(replaceName, replaceValue);

        FoxObject fobject = new FoxObject(objectData);
        Value retVal = new StaticVal(Value_t.OBJECT, fobject);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getNextItem(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETNEXTITEM, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof IteratorASTVisitor)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument to getNext should be an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) val.getData();
        if (!astVisitor.hasNext()) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Statement iterator out of bounds.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        ASTNode curItem = astVisitor.getNextItem();
        Value retVal;
        if (curItem instanceof ExpressionStatement) { // When an ExpressionStatement is next, return its expression instead
            Expression expr = ((ExpressionStatement) curItem).getExpression();
            if (expr instanceof ParenthesisExpression) {
                expr = ((ParenthesisExpression) expr).getExpression();
            }
            retVal = new StaticVal(Value_t.AST, expr);
        } else {
            retVal = new StaticVal(Value_t.AST, curItem);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getPrevItem(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETPREVITEM, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof IteratorASTVisitor)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument to getNext should be an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) val.getData();
        if (!astVisitor.hasPrev()) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Statement iterator out of bounds.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        ASTNode curItem = astVisitor.getPrevItem();
        Value retVal;
        if (curItem instanceof ExpressionStatement) {  // When an ExpressionStatement is next, return its expression instead
            Expression expr = ((ExpressionStatement) curItem).getExpression();
            if (expr instanceof ParenthesisExpression) {
                expr = ((ParenthesisExpression) expr).getExpression();
            }
            retVal = new StaticVal(Value_t.AST, expr);
        } else {
            retVal = new StaticVal(Value_t.AST, curItem);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void hasNextItem(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.HASNEXTITEM, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof IteratorASTVisitor)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument to getNext should be an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) val.getData();
        StaticVal retVal;
        if (!astVisitor.hasNext()) {
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.FALSE);
        } else {
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.TRUE);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void hasPrevItem(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.HASPREVITEM, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof IteratorASTVisitor)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument to getNext should be an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) val.getData();
        StaticVal retVal;
        if (!astVisitor.hasPrev()) {
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.FALSE);
        } else {
            retVal = new StaticVal(Value_t.BOOLEAN, Boolean.TRUE);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void addItemBefore(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ADDITEMBEFORE, env)) {
            return;
        }
        Value iterVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value stmtVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if (!(iterVal.getData() instanceof IteratorASTVisitor) || !(stmtVal.getData() instanceof Statement) && !(stmtVal.getData() instanceof ArrayList<?>)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "addItem requires an iterator object and a statement list AST.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) iterVal.getData();
        if (stmtVal.getData() instanceof ArrayList) {
            ArrayList statementList = (ArrayList) stmtVal.getData();
            if (!(statementList.get(0) instanceof Statement)) {
                StaticVal retVal = new StaticVal(Value_t.ERROR, "addItem requires an iterator object and a statement list AST");
                ((FunctionEnv) env).setReturnVal(retVal);
                return;
            }
            astVisitor.addStatementBefore(statementList);
        } else {
            Statement newStmt = (Statement) stmtVal.getData();
            astVisitor.addStatementBefore(newStmt);
        }

        Value retVal = NULL;
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void addItemAfter(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ADDITEMAFTER, env)) {
            return;
        }
        Value iterVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value stmtVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if (!(iterVal.getData() instanceof IteratorASTVisitor) || !(stmtVal.getData() instanceof Statement) && !(stmtVal.getData() instanceof ArrayList<?>)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "addItem requires an iterator object and a statement list AST.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) iterVal.getData();
        if (stmtVal.getData() instanceof ArrayList) {
            ArrayList statementList = (ArrayList) stmtVal.getData();
            if (!(statementList.get(0) instanceof Statement)) {
                StaticVal retVal = new StaticVal(Value_t.ERROR, "addItem requires an iterator object and a statement list AST");
                ((FunctionEnv) env).setReturnVal(retVal);
                return;
            }
            astVisitor.addStatementAfter(statementList);
        } else {
            Statement newStmt = (Statement) stmtVal.getData();
            astVisitor.addStatementAfter(newStmt);
        }

        Value retVal = NULL;
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void removeItem(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.REMOVEITEM, env)) {
            return;
        }
        Value iterVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(iterVal.getData() instanceof IteratorASTVisitor)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "removeItem requires an iterator object.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) iterVal.getData();
        astVisitor.removeCurrentStatement();

        Value retVal = NULL;
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void replaceItem(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.REPLACEITEM, env)) {
            return;
        }
        Value iterVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value stmtVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if (!(iterVal.getData() instanceof IteratorASTVisitor) || !(stmtVal.getData() instanceof Statement) && !(stmtVal.getData() instanceof ArrayList<?>)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "replaceItem requires an iterator object and a statement list AST.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IteratorASTVisitor astVisitor = (IteratorASTVisitor) iterVal.getData();
        if (stmtVal.getData() instanceof ArrayList) {
            ArrayList statementList = (ArrayList) stmtVal.getData();
            if (!(statementList.get(0) instanceof Statement)) {
                StaticVal retVal = new StaticVal(Value_t.ERROR, "replaceItem requires an iterator object and a statement list AST");
                ((FunctionEnv) env).setReturnVal(retVal);
                return;
            }
            astVisitor.setCurrentStatement(statementList);
        } else {
            Statement newStmt = (Statement) stmtVal.getData();
            astVisitor.setCurrentStatement(newStmt);
        }

        Value retVal = NULL;
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isStatement(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISSTATEMENT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        // instanceof is needed here because all statements are subclasses of Statement
        Value retVal = (val.getData() instanceof Statement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isIfStatement(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISIFSTATEMENT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof IfStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isForStatement(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISFORSTATEMENT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ForStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isWhileStatement(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISWHILESTATEMENT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof WhileStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isReturnStatement(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISRETURNSTATEMENT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ReturnStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isBreakStatement(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISBREAKSTATEMENT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof BreakStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isContinueStatement(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISCONTINUESTATEMENT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ContinueStatement) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isFunctionDefinition(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISFUNCTIONDEFINITION, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof FunctionDef) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISEXPRESSION, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof Expression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isAssignmentExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISASSIGNMENTEXPRESSION, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof AssignmentExpression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isBinaryExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISBINARYEXPRESSION, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof BinaryExpression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isUnaryExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISUNARYEXPRESSION, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof UnaryExpression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaSyntax(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISMETASYNTAX, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaSyntax) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaExecute(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISMETAEXECUTE, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaExecute) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaRun(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISMETARUN, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaRun) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaEval(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISMETAEVAL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaEval) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMetaToText(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISMETATOTEXT, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof MetaToText) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isIdentifier(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISIDENTIFIER, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof IdentifierExpression) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isMember(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISMEMBER, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof Member) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isCall(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISCALL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof Call) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isLvalueCall(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISLVALUECALL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof LvalueCall) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isExtendedCall(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISEXTENDEDCALL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ExtendedCall) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isAnonymousCall(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISANONYMOUSCALL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof AnonymousFunctionCall) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isObjectDefinition(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISOBJECTDEFINITION, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ObjectDefinition) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isArrayDefinition(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISARRAYDEFINITION, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof ArrayDef) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isIntegerLiteral(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISINTEGERLITERAL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof IntegerLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isDoubleLiteral(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISDOUBLELITERAL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof DoubleLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isStringLiteral(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISSTRINGLITERAL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof StringLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isNullLiteral(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISNULLLITERAL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof NullLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isTrueLiteral(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISTRUELITERAL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof TrueLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void isFalseLiteral(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ISFALSELITERAL, env)) {
            return;
        }
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value retVal = (val.getData() instanceof FalseLiteral) ? new StaticVal<>(Value_t.BOOLEAN, true) : new StaticVal<>(Value_t.BOOLEAN, false);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    // ASTNode's getters for fox
    public static void getExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETEXPRESSION, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!val.getType().equals(Value_t.AST)) {
            retVal = new StaticVal(Value_t.ERROR, "getExpression requires an AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        ASTNode ast = (ASTNode) val.getData();
        Expression retExpression = null;
        if (ast instanceof IfStatement) {
            retExpression = ((IfStatement) ast).getExpression();

        } else if (ast instanceof ForStatement) {
            retExpression = ((ForStatement) ast).getExpression();

        } else if (ast instanceof WhileStatement) {
            retExpression = ((WhileStatement) ast).getExpression();

        } else if (ast instanceof ReturnStatement) {
            retExpression = ((ReturnStatement) ast).getExpression();

        } else if (ast instanceof AssignmentExpression) {
            retExpression = ((AssignmentExpression) ast).getExpression();

        } else if (ast instanceof UnaryExpression) {
            retExpression = ((UnaryExpression) ast).getExpression();

        } else if (ast instanceof MetaSyntax) {
            retExpression = ((MetaSyntax) ast).getExpression();

        } else if (ast instanceof MetaExecute) {
            retExpression = ((MetaExecute) ast).getExpression();

        } else if (ast instanceof MetaRun) {
            retExpression = ((MetaRun) ast).getExpression();

        } else if (ast instanceof MetaEval) {
            retExpression = ((MetaEval) ast).getExpression();

        } else if (ast instanceof MetaToText) {
            retExpression = ((MetaToText) ast).getExpression();
        }

        if (retExpression instanceof ParenthesisExpression) {
            retExpression = ((ParenthesisExpression) retExpression).getExpression();
        }

        if (retExpression == null) {
            retVal = utils.Constants.NULL;
        } else {
            retVal = new StaticVal(Value_t.AST, retExpression);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void setExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SETEXPRESSION, env)) {
            return;
        }
        Value retVal = utils.Constants.NULL;
        Value stmtVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value exprVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if (!(stmtVal.getData() instanceof Statement) && !(stmtVal.getData() instanceof Expression)
                || !(exprVal.getData() instanceof Expression)) {
            retVal = new StaticVal(Value_t.ERROR, "setExpression requires a statement AST and an expression AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        ASTNode stmtast = (ASTNode) stmtVal.getData();
        Expression expr = (Expression) exprVal.getData();

        if (stmtast instanceof IfStatement) {
            ((IfStatement) stmtast).setExpression(expr);

        } else if (stmtast instanceof ForStatement) {
            ((ForStatement) stmtast).setExpression(expr);

        } else if (stmtast instanceof WhileStatement) {
            ((WhileStatement) stmtast).setExpression(expr);

        } else if (stmtast instanceof ReturnStatement) {
            ((ReturnStatement) stmtast).setExpression(expr);

        } else if (stmtast instanceof AssignmentExpression) {
            ((AssignmentExpression) stmtast).setExpression(expr);

        } else if (stmtast instanceof UnaryExpression) {
            ((UnaryExpression) stmtast).setExpression(expr);

        } else if (stmtast instanceof MetaSyntax) {
            ((MetaSyntax) stmtast).setExpression(expr);

        } else if (stmtast instanceof MetaExecute) {
            ((MetaExecute) stmtast).setExpression(expr);

        } else if (stmtast instanceof MetaRun) {
            ((MetaRun) stmtast).setExpression(expr);

        } else if (stmtast instanceof MetaEval) {
            ((MetaEval) stmtast).setExpression(expr);

        } else if (stmtast instanceof MetaToText) {
            ((MetaToText) stmtast).setExpression(expr);

        } else {
            retVal = new StaticVal(Value_t.ERROR, "setExpression's first argument should be an expression AST");
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void addField(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.ADDFIELD, env)) {
            return;
        }
        Value retVal = utils.Constants.NULL;
        Value objectVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value fieldKey = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        Value fieldValue = env.getActualArgument(LIBRARY_FUNC_ARG + 2);

        if (!objectVal.getType().equals(Value_t.AST)
                || !(objectVal.getData() instanceof ObjectDefinition)) {
            retVal = new StaticVal(Value_t.ERROR, "addField first argument must be an ObjectDefinition AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
 

        if (!(fieldKey.getData() instanceof Expression) || !((fieldValue.getData() instanceof FunctionDef) 
            || (fieldValue.getData() instanceof Expression))) {
            retVal = new StaticVal(Value_t.ERROR, "addField second and third argument must be an Expression AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        if(fieldValue.getData() instanceof FunctionDef)
            ((ObjectDefinition) objectVal.getData()).getIndexedElementList().add(new IndexedElement((Expression) fieldKey.getData(), (FunctionDef) fieldValue.getData()));
        else
            ((ObjectDefinition) objectVal.getData()).getIndexedElementList().add(new IndexedElement((Expression) fieldKey.getData(), (Expression) fieldValue.getData()));
    }

    public static void getLeftExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETLEFTEXPRESSION, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof BinaryExpression)) {
            retVal = new StaticVal(Value_t.ERROR, "getLeftExpression requires a BinaryExpression AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        Expression leftExpression = ((BinaryExpression) val.getData()).getExpression1();
        if (leftExpression instanceof ParenthesisExpression) {
            leftExpression = ((ParenthesisExpression) leftExpression).getExpression();
        }
        retVal = new StaticVal(Value_t.AST, leftExpression);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void setLeftExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SETLEFTEXPRESSION, env)) {
            return;
        }

        Value retVal = NULL;
        Value binaryExpr = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value newExpr = env.getActualArgument(LIBRARY_FUNC_ARG + 1);

        if (!(binaryExpr.getData() instanceof BinaryExpression)
                || (!newExpr.getType().equals(Value_t.AST)
                && !(newExpr.getData() instanceof Expression))) {

            String msg = "Requires a BinaryExpression and an Expression AST.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        ((BinaryExpression) binaryExpr.getData()).setExpression1((Expression) newExpr.getData());

        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getRightExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETRIGHTEXPRESSION, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof BinaryExpression)) {
            retVal = new StaticVal(Value_t.ERROR, "getRightExpression requires a BinaryExpression AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        Expression rightExpression = ((BinaryExpression) val.getData()).getExpression2();
        if (rightExpression instanceof ParenthesisExpression) {
            rightExpression = ((ParenthesisExpression) rightExpression).getExpression();
        }
        retVal = new StaticVal(Value_t.AST, rightExpression);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void setRightExpression(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SETRIGHTEXPRESSION, env)) {
            return;
        }

        Value retVal = NULL;
        Value binaryExpr = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value newExpr = env.getActualArgument(LIBRARY_FUNC_ARG + 1);

        if (!(binaryExpr.getData() instanceof BinaryExpression)
                || (!newExpr.getType().equals(Value_t.AST)
                && !(newExpr.getData() instanceof Expression))) {

            String msg = "Requires a BinaryExpression and an Expression AST.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        ((BinaryExpression) binaryExpr.getData()).setExpression2((Expression) newExpr.getData());

        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getIdentifier(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETIDENTIFIER, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof IdentifierExpression)) {
            retVal = new StaticVal(Value_t.ERROR, "getIdentifier requires an IdentifierExpression AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        IdentifierExpression idExpr = (IdentifierExpression) val.getData();
        retVal = new StaticVal(Value_t.STRING, idExpr.getIdentifier());
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void setIdentifier(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SETIDENTIFIER, env)) {
            return;
        }
        Value retVal;
        Value idVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value newidVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if (!(idVal.getData() instanceof IdentifierExpression)
                || !(newidVal.getType().equals(Value_t.STRING))) {
            retVal = new StaticVal(Value_t.ERROR, "setIdentifier requires an IdentifierExpression AST and a string");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        IdentifierExpression ast = (IdentifierExpression) idVal.getData();
        String newid = (String) newidVal.getData();
        ast.setIdentifier(newid);
    }

    public static void getLvalue(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETLVALUE, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!val.getType().equals(Value_t.AST)) {
            retVal = new StaticVal(Value_t.ERROR, "getLvalue requires an AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        ASTNode ast = (ASTNode) val.getData();
        Lvalue retLvalue = null;

        if (ast instanceof AssignmentExpression) {
            retLvalue = ((AssignmentExpression) ast).getLvalue();
        } else if (ast instanceof LvalueCall) {
            retLvalue = ((LvalueCall) ast).getLvalue();
        } else if (ast instanceof Member) {
            retLvalue = ((Member) ast).getLvalue();
        } else if (ast instanceof UnaryExpression) {
            retLvalue = ((UnaryExpression) ast).getLvalue();
        }

        if (retLvalue == null) {
            retVal = utils.Constants.NULL;
        } else {
            retVal = new StaticVal(Value_t.AST, retLvalue);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void setLvalue(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SETLVALUE, env)) {
            return;
        }
        Value retVal;
        Value exprVal = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value lvalueVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if (!(exprVal.getData() instanceof Expression)
                || !(lvalueVal.getData() instanceof Lvalue)) {
            retVal = new StaticVal(Value_t.ERROR, "setLvalue requires an expression AST and an Lvalue AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        Expression ast = (Expression) exprVal.getData();
        Lvalue lvalue = (Lvalue) lvalueVal.getData();
        if (ast instanceof AssignmentExpression) {
            ((AssignmentExpression) ast).setLvalue(lvalue);
        } else if (ast instanceof LvalueCall) {
            ((LvalueCall) ast).setLvalue(lvalue);
        } else if (ast instanceof Member) {
            ((Member) ast).setLvalue(lvalue);
        } else if (ast instanceof UnaryExpression) {
            ((UnaryExpression) ast).setLvalue(lvalue);
        }
    }

    public static void getElseStatement(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETELSESTATEMENT, env)) {
            return;
        }
        Value retVal = NULL;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof IfStatement)) {
            retVal = new StaticVal(Value_t.ERROR, "getElseStatement requires an IfStatement AST");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        Statement elseStmt = ((IfStatement) val.getData()).getElseStatement();
        if (elseStmt != null) {
            retVal = new StaticVal(Value_t.AST, elseStmt);
        }
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getOperator(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETOPERATOR, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof BinaryExpression)
                && !(val.getData() instanceof UnaryExpression)) {
            String msg = "getOperator requires a BinaryExpression or UnaryExpression.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        String operator;
        if (val.getData() instanceof BinaryExpression) {
            operator = ((BinaryExpression) val.getData()).getOperator().toString();
        } else {
            operator = ((UnaryExpression) val.getData()).getOperator().toString();
        }

        retVal = new StaticVal(Value_t.STRING, operator);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void setOperator(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SETOPERATOR, env)) {
            return;
        }
        Value retVal = NULL;
        Value expr = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value op = env.getActualArgument(LIBRARY_FUNC_ARG + 1);

        if ((!(expr.getData() instanceof BinaryExpression)
                && !(expr.getData() instanceof UnaryExpression))
                || !(op.getData() instanceof String)) {
            String msg = "Requires a BinaryExpression or "
                    + "UnaryExpression and a String which describes the operator.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        Operator operator = Operator.toOperator((String) op.getData());
        if (operator == null) {
            String msg = "The given string '" + op.getData()
                    + "' could not parsed to a valid Operator.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        if (expr.getData() instanceof BinaryExpression) {
            ((BinaryExpression) expr.getData()).setOperator(operator);
        } else {
            ((UnaryExpression) expr.getData()).setOperator(operator);
        }

        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getFoxType(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETFOXTYPE, env)) {
            return;
        }

        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);

        retVal = new StaticVal(Value_t.STRING, val.getType().toString());
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getAstType(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETASTTYPE, env)) {
            return;
        }

        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);

        if (!(val.getType().equals(Value_t.AST))) {
            String msg = "Requires parameter type of AST.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        retVal = new StaticVal(Value_t.STRING, val.getData().getClass().toString().replace("class ast", "Ast"));
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getFunctionName(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETFUNCTIONNAME, env)) {
            return;
        }

        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof FunctionDef)
                && !(val.getData() instanceof FunctionDefExpression)) {

            String msg = "Requires a FunctionDef or FunctionDefExpression.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        ASTNode function;

        if (val.getData() instanceof FunctionDefExpression) {
            function = ((FunctionDefExpression) val.getData()).getFunctionDef();
        } else {
            function = (FunctionDef) val.getData();
        }

        retVal = new StaticVal(Value_t.STRING, ((FunctionDef) function).getFuncName());
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void setFunctionName(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.SETFUNCTIONNAME, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        Value newVal = env.getActualArgument(LIBRARY_FUNC_ARG + 1);
        if (!(val.getData() instanceof FunctionDef)
                && !(val.getData() instanceof FunctionDefExpression)
                || !(newVal.getType().equals(Value_t.STRING))) {

            String msg = "Requires a FunctionDef or FunctionDefExpression and a string.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        FunctionDef function;
        if (val.getData() instanceof FunctionDefExpression) {
            function = ((FunctionDefExpression) val.getData()).getFunctionDef();
        } else {
            function = (FunctionDef) val.getData();
        }
        String newName = (String) newVal.getData();
        function.setFuncName(newName);
    }

    public static void getFunctionArgs(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETFUNCTIONARGS, env)) {
            return;
        }

        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof FunctionDef)
                && !(val.getData() instanceof FunctionDefExpression)) {

            String msg = "Requires a FunctionDef or FunctionDefExpression.";
            retVal = new StaticVal(Value_t.ERROR, msg);
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }

        ASTNode function;

        if (val.getData() instanceof FunctionDefExpression) {
            function = ((FunctionDefExpression) val.getData()).getFunctionDef();
        } else {
            function = (FunctionDef) val.getData();
        }

        ArrayList<IdentifierExpression> args = ((FunctionDef) function).getArguments();
        FoxArray foxArray = new FoxArray();
        args.stream().forEach((arg) -> {
            foxArray.add(new StaticVal(Value_t.STRING, arg.getIdentifier()));
        });

        retVal = new StaticVal(Value_t.TABLE, foxArray);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getLine(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETLINE, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof ASTNode)) {
            retVal = new StaticVal(Value_t.ERROR, "getLine requires an AST node");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        ASTNode astNode = (ASTNode) val.getData();
        int line = astNode.getLine();
        retVal = new StaticVal<>(Value_t.INTEGER, line);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void getColumn(Environment env) {
        if (!checkArgumentsNum(LibraryFunction_t.GETCOLUMN, env)) {
            return;
        }
        Value retVal;
        Value val = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(val.getData() instanceof ASTNode)) {
            retVal = new StaticVal(Value_t.ERROR, "getColumn requires an AST node");
            ((FunctionEnv) env).setReturnVal(retVal);
            return;
        }
        ASTNode astNode = (ASTNode) val.getData();
        int col = astNode.getColumn();
        retVal = new StaticVal<>(Value_t.INTEGER, col);
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

    private static Statement getStatementArgument(Environment env) {
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (!(value.getData() instanceof Statement)) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument must be a statement AST.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;
        }
        Statement stmt = (Statement) value.getData();
        return stmt;
    }

}
