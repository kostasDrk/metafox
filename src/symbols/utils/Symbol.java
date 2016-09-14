/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbols.utils;

import ast.IdentifierExpression;

public class Symbol {

    private final IdentifierExpression _idExpression;

    public Symbol(IdentifierExpression idExpression) {
        _idExpression = idExpression;
    }

    public Symbol(String idExpression) {
        _idExpression = new IdentifierExpression(idExpression);
    }

    public IdentifierExpression getIdExpression() {
        return _idExpression;
    }

    public String getName() {
        return _idExpression.getIdentifier();
    }

}
