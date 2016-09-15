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
 * CLASS Block
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class Block extends Statement {

    private ArrayList<Statement> _statementList;

    public Block() {
        _statementList = new ArrayList<>();
    }

    public Block(ArrayList<Statement> statementList) {
        _statementList = statementList;
    }

    public ArrayList<Statement> getStatementList() {
        return _statementList;
    }

    public void setStatementList(ArrayList<Statement> statementList) {
        _statementList = statementList;
    }

    public void addStatement(Statement stmt, int pos) {
        _statementList.add(pos, stmt);
    }

    public void addStatements(ArrayList<Statement> statementList, int pos) {
        for (Statement curstmt : statementList) {
            addStatement(curstmt, pos);
            pos++;
        }
    }

    public void prependStatement(Statement stmt) {
        addStatement(stmt, 0);
    }

    public void prependStatements(ArrayList<Statement> statementList) {
        addStatements(statementList, 0);
    }

    public void appendStatement(Statement stmt) {
        int pos = this.getStatementList().size() - 1;
        if (_statementList.get(pos) instanceof ReturnStatement) {
            return;
        } else {
            addStatement(stmt, pos + 1);
        }
    }

    public void appendStatements(ArrayList<Statement> statementList) {
        int pos = this.getStatementList().size() - 1;
        if (_statementList.get(pos) instanceof ReturnStatement) {
            return;
        } else {
            addStatements(statementList, pos + 1);
        }
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
