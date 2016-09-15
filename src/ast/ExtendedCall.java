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
 * CLASS ExtendedCall
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class ExtendedCall extends Call {

    private Call _call;
    private NormCall _normCall;
    private LvalueCall _lvalueCall;

    public ExtendedCall(Call call, NormCall normCall) {
        _call = call;
        _normCall = normCall;
        _lvalueCall = null;
    }

    public Call getCall() {
        return _call;
    }

    public void setCall(Call call) {
        _call = call;
    }

    public NormCall getNormCall() {
        return _normCall;
    }

    public void setNormCall(NormCall normCall) {
        _normCall = normCall;
    }

    public LvalueCall getLvalueCall() {
        return _lvalueCall;
    }

    public void setLvalueCall(String lvalue, NormCall normCall) {
        _lvalueCall = new LvalueCall(new IdentifierExpression(lvalue), normCall);
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
