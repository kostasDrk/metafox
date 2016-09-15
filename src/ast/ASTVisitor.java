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
 * CLASS ASTVisitor 
 * Abstract syntax tree visitor.
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */

public interface ASTVisitor {

    Value visit(Program node) throws ASTVisitorException;

    Value visit(AssignmentExpression node) throws ASTVisitorException;

    Value visit(ExpressionStatement node) throws ASTVisitorException;

    Value visit(IdentifierExpression node) throws ASTVisitorException;

    Value visit(IdentifierExpressionGlobal node) throws ASTVisitorException;

    Value visit(IdentifierExpressionLocal node) throws ASTVisitorException;

    Value visit(ParenthesisExpression node) throws ASTVisitorException;

    Value visit(BinaryExpression node) throws ASTVisitorException;

    Value visit(UnaryExpression node) throws ASTVisitorException;

    Value visit(Member node) throws ASTVisitorException;

    Value visit(ExtendedCall node) throws ASTVisitorException;

    Value visit(LvalueCall node) throws ASTVisitorException;

    Value visit(NormCall node) throws ASTVisitorException;

    Value visit(MethodCall node) throws ASTVisitorException;

    Value visit(Block node) throws ASTVisitorException;

    Value visit(ArrayDef node) throws ASTVisitorException;

    Value visit(FunctionDef node) throws ASTVisitorException;

    Value visit(FunctionDefExpression node) throws ASTVisitorException;

    Value visit(AnonymousFunctionCall node) throws ASTVisitorException;

    Value visit(IntegerLiteral node) throws ASTVisitorException;

    Value visit(DoubleLiteral node) throws ASTVisitorException;

    Value visit(StringLiteral node) throws ASTVisitorException;

    Value visit(NullLiteral node) throws ASTVisitorException;

    Value visit(TrueLiteral node) throws ASTVisitorException;

    Value visit(FalseLiteral node) throws ASTVisitorException;

    Value visit(IfStatement node) throws ASTVisitorException;

    Value visit(WhileStatement node) throws ASTVisitorException;

    Value visit(ForStatement node) throws ASTVisitorException;

    Value visit(BreakStatement node) throws ASTVisitorException;

    Value visit(ContinueStatement node) throws ASTVisitorException;

    Value visit(ReturnStatement node) throws ASTVisitorException;

    Value visit(ObjectDefinition node) throws ASTVisitorException;

    Value visit(IndexedElement node) throws ASTVisitorException;

    Value visit(MetaSyntax node) throws ASTVisitorException;

    Value visit(MetaEscape node) throws ASTVisitorException;

    Value visit(MetaExecute node) throws ASTVisitorException;

    Value visit(MetaRun node) throws ASTVisitorException;

    Value visit(MetaEval node) throws ASTVisitorException;

    Value visit(MetaToText node) throws ASTVisitorException;
}
