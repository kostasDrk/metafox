/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolTable;

import ast.ASTVisitorException;
import symbolTable.libraryFunctions.LibraryFunctions;

import symbolTable.entries.SymTableEntryType;
import symbolTable.entries.ASymTableEntry;
import symbolTable.entries.GlobalVariableEntry;
import symbolTable.entries.LocalVariableEntry;
import symbolTable.entries.LibraryFunctionEntry;
import symbolTable.entries.UserFunctionEntry;

import java.util.*;

/**
 *
 * @author Default
 */
public class SymbolTable {

    private static final int GLOBAL_SCOPE = 0;

    private final HashMap<Integer, ASymTableEntry> _scopeLists;
    private final HashMap<String, ArrayList<ASymTableEntry>> _table;

    public SymbolTable() {
        _scopeLists = new HashMap<>();
        _table = new HashMap<>();

        //Insert all the library function in symbol table.
        for (LibraryFunctions libraryFunction : LibraryFunctions.values()) {
            insertSymbolTable(new LibraryFunctionEntry(libraryFunction.name()));
        }

    }

    public ASymTableEntry lookupGlobalScope(String name, Integer line) throws ASTVisitorException {

        ASymTableEntry firstElement = _scopeLists.get(0);

        while (firstElement != null) {
            if (firstElement.getName().equals(name)) {
                return firstElement;
            } else {
                firstElement = firstElement.getNextScopeListNode();
            }
        }
        throw new ASTVisitorException("Global variable: " + name + " doesn't exist" + " in line " + line);

    }

    /*In this function we look up in Symbol Table for formal and function definition
     */
    public void lookUpLocalScope(String name, Integer scope, SymTableEntryType type, Integer line) throws ASTVisitorException {

        ASymTableEntry tempElement = _scopeLists.get(scope);
        while (tempElement != null) {
            if (tempElement.getName().equals(name) && tempElement.isActive()) {
                break;
            } else {
                tempElement = tempElement.getNextScopeListNode();
            }
        }
        if (LibraryFunctions.isLibraryFunction(name)) {
            throw new ASTVisitorException("Error: Collision with library function " + name + " in line " + line);
        }
        if (tempElement != null) {
            throw new ASTVisitorException("Error: " + name + " in line " + line + " is " + tempElement.getClass());

        }

    }

    public void hide(Integer scope) {

        ASymTableEntry tempElement = _scopeLists.get(scope);

        while (tempElement != null) {

            tempElement.setIsActive(false);
            tempElement = tempElement.getNextScopeListNode();
        }

    }

    public ASymTableEntry lookUpVariable(String name, Integer scope, Integer line) throws ASTVisitorException {

        ASymTableEntry element = null;
        Boolean foundUserFunction = false;
        ASymTableEntry tempElement = _scopeLists.get(scope);
        Integer tempScope = scope;

        while (tempElement != null && tempScope != -1) {
            while (tempElement != null) {

                if (tempElement.getName().equals(name) && element.isActive()) {
                    element = tempElement;
                    break;
                } else {
                    if (((tempElement instanceof UserFunctionEntry))
                            && (tempElement.isActive() == true)) {
                        foundUserFunction = true;
                    }
                    tempElement = tempElement.getNextScopeListNode();
                }

            }
            if (element != null) {
                break;
            }
            tempScope--;
        }

        if (element == null) {
            if (scope == GLOBAL_SCOPE) {
                element = new GlobalVariableEntry(name);
            } else {
                element = new LocalVariableEntry(name, scope);
            }

            insertSymbolTable(element);

        } else if (element.getScope() != GLOBAL_SCOPE
                && foundUserFunction
                && scope != element.getScope()
                && element instanceof UserFunctionEntry) {
            throw new ASTVisitorException("Cannot access symbol: " + name + " in line " + line + " in scope " + scope);
        }

        return element;
    }

    private void insertSymbolTable(ASymTableEntry _newEntry) {

        if (_table.get(_newEntry.getName()) == null) {
            ArrayList<ASymTableEntry> ASymTableEntryList = new ArrayList<ASymTableEntry>();
            ASymTableEntryList.add(_newEntry);
            _table.put(_newEntry.getName(), ASymTableEntryList);

        } else {
            _table.get(_newEntry.getName()).add(_newEntry);

        }

        if (_scopeLists.get(_newEntry.getScope()) == null) {
            _scopeLists.put(_newEntry.getScope(), _newEntry);

        } else {
            ASymTableEntry tempElement = _scopeLists.get(_newEntry.getScope());
            while (tempElement.getNextScopeListNode() != null) {

                tempElement = tempElement.getNextScopeListNode();
            }
            tempElement.setNextScopeListNode(_newEntry);
        }

    }
}
