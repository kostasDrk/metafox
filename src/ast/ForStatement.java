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
 * CLASS ForStatement
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class ForStatement extends Statement {

    private ArrayList<Expression> _expressionList1;
    private ArrayList<Expression> _expressionList2;
    private Expression _expression;
    private Statement _statement;

    public ForStatement(ArrayList<Expression> expressionList1, Expression expression, ArrayList<Expression> expressionList2, Statement statement) {
        if (expressionList1 == null) {
            _expressionList1 = new ArrayList<>();
        } else {
            _expressionList1 = expressionList1;
        }

        this._expression = expression;

        if (expressionList2 == null) {
            _expressionList2 = new ArrayList<>();
        } else {
            _expressionList2 = expressionList2;
        }
        _statement = statement;
    }

    public ArrayList<Expression> getExpressionList1() {
        return _expressionList1;
    }

    public void setExpressionList1(ArrayList<Expression> expressionList1) {
        _expressionList1 = expressionList1;
    }

    public ArrayList<Expression> getExpressionList2() {
        return _expressionList2;
    }

    public void setExpressionList2(ArrayList<Expression> expressionList2) {
        _expressionList2 = expressionList2;
    }

    public Expression getExpression() {
        return _expression;
    }

    public void setExpression(Expression expression) {
        _expression = expression;
    }

    public Statement getStatement() {
        return _statement;
    }

    public void setStatement(Statement statement) {
        _statement = statement;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
