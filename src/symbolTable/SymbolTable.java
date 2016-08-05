/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolTable;

import ast.ASTVisitorException;
import libraryFunctions.LibraryFunctions;

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
    private int _maxEnteredScope;

    public SymbolTable() {
        _scopeLists = new HashMap<>();
        _table = new HashMap<>();
        _maxEnteredScope = GLOBAL_SCOPE;

        //Insert all the library function in symbol table.
        for (LibraryFunctions libraryFunction : LibraryFunctions.values()) {
            insertSymbolTable(new LibraryFunctionEntry(libraryFunction.toString()));
        }

    }

    public ASymTableEntry lookupGlobalScope(String name) {

        ASymTableEntry firstElement = _scopeLists.get(GLOBAL_SCOPE);

        while (firstElement != null) {
            if (firstElement.getName().equals(name)) {
                return firstElement;
            }
            
            firstElement = firstElement.getNextScopeListNode();
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

        while (tempScope >= GLOBAL_SCOPE) {
            while (tempElement != null) {
                if (tempElement.getName().equals(name) && tempElement.isActive()) {
                    element = tempElement;
                    break;
                } else if (!tempElement.getName().equals(name) && tempElement.isActive()) {
                    if (tempElement instanceof UserFunctionEntry) {
                        foundUserFunction = true;
                    }
                }
                tempElement = tempElement.getNextScopeListNode();
            }
            if (element != null) {
                break;
            }
            tempScope--;
            tempElement = _scopeLists.get(tempScope);
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

        keepMaxScope(_newEntry.getScope());

    }

    private void keepMaxScope(int scope) {
        if (scope > _maxEnteredScope) {
            _maxEnteredScope = scope;
        }
    }

    public void printAll() {
        System.out.println("--------------------------------------------------------------");
        System.out.println("---------------------Symbol Table Entries---------------------");
        System.out.println("--------------------------------------------------------------");

        for (int scope = 0; scope <= _maxEnteredScope; scope++) {
            System.out.println("");
            System.out.println("-------------------------- Scope: " + scope + " --------------------------");

            ArrayList<ASymTableEntry> scopeList = new ArrayList<>();
            ASymTableEntry tmpElement = _scopeLists.get(scope);

            while (tmpElement != null) {
                scopeList.add(tmpElement);
                tmpElement = tmpElement.getNextScopeListNode();
            }

            Collections.reverse(scopeList);
            for (ASymTableEntry tmpList : scopeList) {
                System.out.println(tmpList);
            }

            System.out.println("--------------------------------------------------------------");
        }
    }
}
