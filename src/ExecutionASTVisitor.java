
import ast.ASTVisitor;
import ast.ASTVisitorException;
import ast.AnonymousFunctionCall;
import ast.ArrayDef;
import ast.AssignmentExpression;
import ast.BinaryExpression;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayDeque;

import symbolTable.SymbolTable;
import symbolTable.entries.AFunctionEntry;
import symbolTable.entries.ASymTableEntry;
import symbolTable.entries.FormalVariableEntry;
import symbolTable.entries.GlobalVariableEntry;
import symbolTable.entries.LocalVariableEntry;
import symbolTable.entries.UserFunctionEntry;
import symbolTable.libraryFunctions.LibraryFunctions;

public class ExecutionASTVisitor implements ASTVisitor {

    private ArrayDeque<SymbolTable> _envStack;
    private SymbolTable _genv;
    private final SymbolTable _symTable;
    private int _scope;
    private int _inFunction;  
    private int _inLoop;  

    private void enterScopeSpace() {
        System.out.println("EnterScopeSpace");
        _scope++;
    }

    private void exitScopeSpace() {
        System.out.println("ExitScopeSpace");
        _scope--;
    }
    private void enterFunctionSpace() {
        System.out.println("EnterFunctionSpace");
        _inFunction++;
    }

    private void exitFunctionSpace() {
        System.out.println("ExitFunctionSpace");
        _inFunction--;
    }

     private void enterLoopSpace() {
        System.out.println("EnterLoopSpace");
        _inLoop++;
    }

    private void exitLoopSpace() {
        System.out.println("ExitLoopSpace");
        _inLoop--;
    }

    private void hideScopeeSpaceAndExit() {
        System.out.println("HiddeScopeSpaceAndExit");
        _symTable.hide(_scope);
        _scope--;
    }

    public ExecutionASTVisitor() {
        _genv = new SymbolTable();
        _envStack = new ArrayDeque<SymbolTable>();
        _envStack.push(_genv);
        _symTable = _genv;
        _scope = 0;
        _inFunction = 0;
        _inLoop = 0;
    }

    @Override
    public void visit(Program node) throws ASTVisitorException {
        System.out.println("-Program");
        for (Statement stmt : node.getStatements()) {
           if(stmt != null)

            stmt.accept(this);
        }

        this._symTable.printAll();
    }

    @Override
    public void visit(ExpressionStatement node) throws ASTVisitorException {
        System.out.println("-ExpressionStatement");

        node.getExpression().accept(this);

    }

    @Override
    public void visit(AssignmentExpression node) throws ASTVisitorException {
        System.out.println("-AssignmentExpression");

        node.getLvalue().accept(this);
        node.getExpression().accept(this);
    }

    @Override
    public void visit(BinaryExpression node) throws ASTVisitorException {
        System.out.println("-BinaryExpression");

        node.getExpression1().accept(this);
        //System.out.print(" " + node.getOperator() + " ");
        node.getExpression2().accept(this);
    }

    @Override
    public void visit(TermExpressionStmt node) throws ASTVisitorException {
        System.out.println("-TermExpressionStmt");

        node.getExpression().accept(this);

    }

    @Override
    public void visit(UnaryExpression node) throws ASTVisitorException {
        System.out.println("-UnaryExpression");

        //System.out.print(node.getOperator());
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        } else {
             IdentifierExpression name = (IdentifierExpression)node.getLvalue();
             HashMap<String, Object> returnVal = _symTable.lookUpVariable(name.getIdentifier(), _scope);
            ASymTableEntry symTableEntry = (ASymTableEntry) returnVal.get("symbolTableEntry");

            if (symTableEntry != null) {
               
                if (symTableEntry instanceof AFunctionEntry)
                         {

                    String msg = "Using function: "+  name.getIdentifier()
                            + " as lvalue.";
                    ASTUtils.error(node, msg);
                }
            }
            node.getLvalue().accept(this);
        }
    }

    @Override
    public void visit(IdentifierExpression node) throws ASTVisitorException {
        System.out.println("-IdentifierExpression");
        String name = node.getIdentifier();

        //if variable have :: at the front it is "global"
        if (!node.isLocal()) {
            ASymTableEntry symTableEntry = _symTable.lookupGlobalScope(name);
            if (symTableEntry == null) {
                String msg = "Global variable: " + name + " doesn't exist";
                ASTUtils.error(node, msg);
            }

        } else {
            HashMap<String, Object> returnVal = _symTable.lookUpVariable(name, _scope);
            ASymTableEntry symTableEntry = (ASymTableEntry) returnVal.get("symbolTableEntry");
            boolean foundUserFunction = (boolean) returnVal.get("foundUserFunction");

            if (symTableEntry == null) {
                if (!node.isLocal() || this._scope == 0) {
                    symTableEntry = new GlobalVariableEntry(name);
                } else {
                    symTableEntry = new LocalVariableEntry(name, _scope);
                }

                _symTable.insertSymbolTable(symTableEntry);
            } else {
                if (foundUserFunction
                        && !symTableEntry.hasGlobalScope()
                        && !(symTableEntry instanceof AFunctionEntry)
                        && (_scope != symTableEntry.getScope())) {

                    String msg = "Cannot access symbol: " + name
                            + " (in scope " + symTableEntry.getScope() + " ).";
                    ASTUtils.error(node, msg);
                }
            }
        }
    }

    @Override
    public void visit(Member node) throws ASTVisitorException {
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
    }

    @Override
    public void visit(ExtendedCall node) throws ASTVisitorException {
        System.out.println("-ExtendedCall");

        node.getCall().accept(this);

        node.getExpressionList();
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);

        }

    }

    @Override
    public void visit(LvalueCall node) throws ASTVisitorException {
        System.out.println("-LvalueCall");

        node.getLvalue().accept(this);
        node.getCallSuffix().accept(this);
    }

    @Override
    public void visit(AnonymousFunctionCall node) throws ASTVisitorException {
        System.out.println("-AnonymousFunctionCall");

        node.getFunctionDef().accept(this);

        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);
        }

    }

    @Override
    public void visit(NormCall node) throws ASTVisitorException {
        System.out.println("-NormCall");

        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);

        }

    }

    @Override
    public void visit(MethodCall node) throws ASTVisitorException {
        System.out.println("-MethodCall");

        //System.out.print(".." + node.getIdentifier() + "(");
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);

        }

    }

    @Override
    public void visit(ObjectDefinition node) throws ASTVisitorException {
        System.out.println("-ObjectDefinition");

        if (!node.getIndexedElementList().isEmpty()) {
            for (IndexedElement indexed : node.getIndexedElementList()) {
                indexed.accept(this);

            }
        }

    }

    @Override
    public void visit(IndexedElement node) throws ASTVisitorException {
        System.out.println("-IndexedElement");

        node.getExpression1().accept(this);
        node.getExpression2().accept(this);
    }

    @Override
    public void visit(ArrayDef node) throws ASTVisitorException {
        System.out.println("-ArrayDef");
     if(node.getExpressionList()!=null){
        for (Expression expression : node.getExpressionList()) {
            expression.accept(this);

        }
    }

    }

    @Override
    public void visit(Block node) throws ASTVisitorException {
        System.out.println("-Block");

        enterScopeSpace();
        for (Statement stmt : node.getStatementList()) {
            stmt.accept(this);
        }
        hideScopeeSpaceAndExit();
    }

    @Override
    public void visit(FunctionDefExpression node) throws ASTVisitorException {
        System.out.println("-FunctionDefExpression");

        node.getFunctionDef().accept(this);
    }

    @Override
    public void visit(FunctionDef node) throws ASTVisitorException {
        System.out.println("-FunctionDef");

        String name = node.getFuncName();
        /*Function Name*/
        ASymTableEntry symbolTableEntry = _symTable.lookUpLocalScope(name, _scope);
        boolean isLibraryFunction = LibraryFunctions.isLibraryFunction(name);

        if (symbolTableEntry != null) {
            boolean isUserFunction = symbolTableEntry instanceof UserFunctionEntry;
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

        /*Function arguments*/
        enterScopeSpace();
        ArrayList<FormalVariableEntry> args = new ArrayList();
        for (IdentifierExpression argument : node.getArguments()) {
            name = argument.getIdentifier();
            ASymTableEntry entry = _symTable.lookUpLocalScope(name, _scope);

            if (entry != null) {
                String msg = "Redeclaration of Formal-Argument: " + name + ".";
                ASTUtils.error(node, msg);
            }

            if (LibraryFunctions.isLibraryFunction(name)) {
                String msg = "Formal-Argument shadows Library-Function: " + name + ".";
                ASTUtils.error(node, msg);
            }

            FormalVariableEntry formalArg = new FormalVariableEntry(name, _scope);
            args.add(formalArg);

            _symTable.insertSymbolTable(formalArg);
        }
        exitScopeSpace();

        ASymTableEntry symEntry = new UserFunctionEntry(node.getFuncName(), args, _scope);
        _symTable.insertSymbolTable(symEntry);
        enterFunctionSpace();
        node.getBody().accept(this);
        exitFunctionSpace();
    }

    @Override
    public void visit(IntegerLiteral node) throws ASTVisitorException {
        System.out.println("-IntegerLiteral");

    }

    @Override
    public void visit(DoubleLiteral node) throws ASTVisitorException {
        System.out.println("-DoubleLiteral");

    }

    @Override
    public void visit(StringLiteral node) throws ASTVisitorException {
        System.out.println("-StringLiteral");

    }

    @Override
    public void visit(NullLiteral node) throws ASTVisitorException {
        System.out.println("-NullLiteral");

    }

    @Override
    public void visit(TrueLiteral node) throws ASTVisitorException {
        System.out.println("-TrueLiteral");

    }

    @Override
    public void visit(FalseLiteral node) throws ASTVisitorException {
        System.out.println("-FalseLiteral");

    }

    @Override
    public void visit(IfStatement node) throws ASTVisitorException {
        System.out.println("-IfStatement");

        node.getExpression().accept(this);

        node.getStatement().accept(this);
        if (node.getElseStatement() != null) {
            node.getElseStatement().accept(this);
        }
    }

    @Override
    public void visit(WhileStatement node) throws ASTVisitorException {
        System.out.println("-WhileStatement");

        node.getExpression().accept(this);
        enterLoopSpace();
        node.getStatement().accept(this);
        exitLoopSpace();
    }

    @Override
    public void visit(ForStatement node) throws ASTVisitorException {
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
    }

    @Override
    public void visit(BreakStatement node) throws ASTVisitorException {
        System.out.println("-BreakStatement");
         if( _inLoop == 0) {
              
              ASTUtils.error(node, "Use of 'break' while not in a loop.");
        }
    }

    @Override
    public void visit(ContinueStatement node) throws ASTVisitorException {
        System.out.println("-ContinueStatement");
           if( _inLoop == 0) {
              
              ASTUtils.error(node, "Use of 'continue' while not in a loop.");
        }
    }

    @Override
    public void visit(ReturnStatement node) throws ASTVisitorException {

        System.out.println("-ReturnStatement");
        if( _inFunction == 0) {
              
              ASTUtils.error(node, "Use of 'return' while not in a function.");
        }
       if(node.getExpression()!=null)

        node.getExpression().accept(this);
    }
}
