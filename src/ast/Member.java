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
 * CLASS Member
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class Member extends Lvalue {

    private Lvalue _lvalue;
    private final IdentifierExpression _identifier;
    private Call _call;
    private Expression _expression;

    private boolean _isLValue;

    public Member(Lvalue lvalue, IdentifierExpression identifier, Call call, Expression expression) {
        _lvalue = lvalue;
        _identifier = identifier;
        _call = call;
        _expression = expression;
        _isLValue = false;
    }

    public Lvalue getLvalue() {
        return _lvalue;
    }

    public void setLvalue(Lvalue lvalue) {
        _lvalue = lvalue;
    }

    public String getIdentifier() {
        return _identifier != null ? _identifier.getIdentifier() : null;
    }

    public IdentifierExpression getIdentifierExpr() {
        return _identifier;
    }

    public void setIdentifier(String identifier) {
        _identifier.setIdentifier(identifier);
    }

    public Call getCall() {
        return _call;
    }

    public void setCall(Call call) {
        _call = call;
    }

    public Expression getExpression() {
        return _expression;
    }

    public void setExpression(Expression expression) {
        _expression = expression;
    }

    public boolean isLValue() {
        return _isLValue;
    }

    public void setIsLValue() {
        _isLValue = true;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
