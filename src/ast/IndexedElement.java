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
 * CLASS IndexedElement
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class IndexedElement extends ASTNode {

    private Expression _expression1;
    private Expression _expression2;
    private FunctionDef _functionDef;
    private final boolean _isValueExpression;

    public IndexedElement(Expression expression1, Expression expression2) {
        _expression1 = expression1;
        _expression2 = expression2;

        _isValueExpression = Boolean.TRUE;
    }

    public IndexedElement(Expression expression1, FunctionDef functionDef) {
        _expression1 = expression1;
        _functionDef = functionDef;

        _isValueExpression = Boolean.FALSE;
    }

    public Expression getExpression1() {
        return _expression1;
    }

    public void setExpression1(Expression expression1) {
        _expression1 = expression1;
    }

    public Expression getExpression2() {
        return _expression2;
    }

    public void setExpression2(Expression expression2) {
        _expression2 = expression2;
    }

    public FunctionDef getFunctionDef() {
        return _functionDef;
    }

    public void setFunctionDef(FunctionDef functionDef) {
        _functionDef = functionDef;
    }

    public boolean isValueExpression() {
        return _isValueExpression;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
