/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import symbols.value.Value;

public class IdentifierExpressionGlobal extends IdentifierExpression {

    public IdentifierExpressionGlobal(String identifier) {
        super(identifier);
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
