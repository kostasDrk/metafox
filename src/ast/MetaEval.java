/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import symbols.value.Value;

public class MetaEval extends TermExpression {

    private final Expression _expression;
    private final MetaExecute _metaExecute;

    public MetaEval(Expression expression) {
        _expression = expression;
        _metaExecute = new MetaExecute(new MetaRun(expression));
    }

    public Expression getExpression() {
        return _expression;
    }

    public MetaExecute getEvalNode() {
        return _metaExecute;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
