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

import java.util.ArrayList;
import symbols.value.Value;

/**
 * CLASS FunctionDef
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class FunctionDef extends Statement {

    private final IdentifierExpression _funcNameIdentifier;
    private ArrayList<IdentifierExpression> _arguments;
    private Block _body;
    private static int _anonumousFunctionCounter = 0;

    public FunctionDef(String funcName, ArrayList<IdentifierExpression> arguments, Block body) {
        if (funcName.equals("#ANONYMOUS#_")) {
            funcName += _anonumousFunctionCounter;
            _anonumousFunctionCounter++;
        }

        _funcNameIdentifier = new IdentifierExpression(funcName);
        _arguments = arguments;
        _body = body;
    }

    public String getFuncName() {
        return _funcNameIdentifier.getIdentifier();
    }

    public IdentifierExpression getFuncNameIdentifierExpr() {
        return _funcNameIdentifier;
    }

    public void setFuncName(String funcName) {
        _funcNameIdentifier.setIdentifier(funcName);
    }

    public ArrayList<IdentifierExpression> getArguments() {
        return _arguments;
    }

    public void setArguments(ArrayList<IdentifierExpression> arguments) {
        _arguments = arguments;
    }

    public Block getBody() {
        return _body;
    }

    public void setBody(Block body) {
        _body = body;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
