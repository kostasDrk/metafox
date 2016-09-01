/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import symbols.value.Value;

public class MetaRun extends TermExpression {

    private Expression _expression;

    public MetaRun(Expression expression) {
        this._expression = expression;
    }

    public Expression getExpression() {
        return _expression;
    }

    public void setExpression(Expression expression) {
        this._expression = expression;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
