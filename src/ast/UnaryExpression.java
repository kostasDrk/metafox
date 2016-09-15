/**
 * Metafox - A DYNAMIC, INTERPRETED, META-PROGRAMMING LANGUAGE, RUN AND
 * SUPPORTED BY ITS OWN INDEPENDENT INTERPRETER.
 *
 * UNIVERSITY OF CRETE (UOC)
 *
 * COMPUTER SCIENCE DEPARTMENT (UOC)
 *
 * https://www.csd.uoc.gr/
 *
 * CS-540 ADVANCED TOPICS IN PROGRAMMING LANGUAGES DEVELOPMENT
 *
 * LICENCE: This file is part of Metafox. Metafox is free: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 *
 * Metafox is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Metafox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016
 *
 */
package ast;

import symbols.value.Value;

/**
 * CLASS UnaryExpression
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class UnaryExpression extends TermExpression {

    private Operator _operator;
    private Expression _expression;
    private Lvalue _lvalue;
    private final boolean _isLvaluePositionedLeft;

    public UnaryExpression(Operator operator, Expression expression, Lvalue lvalue, boolean isLvaluePositionedLeft) {
        _operator = operator;
        _expression = expression;
        _lvalue = lvalue;
        _isLvaluePositionedLeft = isLvaluePositionedLeft;
    }

    public Operator getOperator() {
        return _operator;
    }

    public void setOperator(Operator operator) {
        _operator = operator;
    }

    public Expression getExpression() {
        return _expression;
    }

    public void setExpression(Expression expression) {
        _expression = expression;
    }

    public Lvalue getLvalue() {
        return _lvalue;
    }

    public void setLvalue(Lvalue lval) {
        _lvalue = lval;
    }

    public boolean isIsLvaluePositionedLeft() {
        return _isLvaluePositionedLeft;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
