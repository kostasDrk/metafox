/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolTable;

import ast.ASTVisitorException;
import symbolTable.libraryFunctions.LibraryFunctions;

import symbolTable.entries.ASymTableEntry;
import symbolTable.entries.LibraryFunctionEntry;
import symbolTable.entries.UserFunctionEntry;

import java.util.*;

/**
 *
 * @author Default
 */
public final class SymbolTable {

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

    public ASymTableEntry lookupGlobalScope(String name) {

        ASymTableEntry firstElement = _scopeLists.get(0);

        while (firstElement != null) {
            if (firstElement.getName().equals(name)) {
                return firstElement;
            } else {
                firstElement = firstElement.getNextScopeListNode();
            }
        }

        return null;
    }

    /*In this function we look up in Symbol Table for formal and function definition
     */
    public ASymTableEntry lookUpLocalScope(String name, Integer scope) throws ASTVisitorException {

        ASymTableEntry tempElement = _scopeLists.get(scope);
        while (tempElement != null) {
            if (tempElement.getName().equals(name) && tempElement.isActive()) {
                break;
            }

            tempElement = tempElement.getNextScopeListNode();
        }

        return tempElement;
    }

    public void hide(Integer scope) {

        ASymTableEntry tempElement = _scopeLists.get(scope);

        while (tempElement != null) {

            tempElement.setIsActive(false);
            tempElement = tempElement.getNextScopeListNode();
        }

    }

    public HashMap<String, Object> lookUpVariable(String name, Integer scope) {
        ASymTableEntry element = null;
        Boolean foundUserFunction = false;
        ASymTableEntry tempElement = _scopeLists.get(scope);
        Integer tempScope = scope;

        while (tempElement != null && tempScope != -1) {
            while (tempElement != null) {

                if (tempElement.getName().equals(name) && tempElement.isActive()) {
                    element = tempElement;
                    break;
                } else if (!tempElement.getName().equals(name) && tempElement.isActive()) {
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

        HashMap<String, Object> returnVal = new HashMap<>();
        returnVal.put("symbolTableEntry", element);
        returnVal.put("foundUserFunction", foundUserFunction);
        return returnVal;
    }

    public void insertSymbolTable(ASymTableEntry _newEntry) {

        if (_table.get(_newEntry.getName()) == null) {
            ArrayList<ASymTableEntry> ASymTableEntryList = new ArrayList<>();
            ASymTableEntryList.add(_newEntry);
            _table.put(_newEntry.getName(), ASymTableEntryList);

        } else {
            _table.get(_newEntry.getName()).add(_newEntry);

        }

        if (_scopeLists.get(_newEntry.getScope()) != null) {
            ASymTableEntry tempElement = _scopeLists.get(_newEntry.getScope());
            _newEntry.setNextScopeListNode(tempElement);

        }
        _scopeLists.put(_newEntry.getScope(), _newEntry);
    }
}
