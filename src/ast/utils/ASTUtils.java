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
package ast.utils;

import ast.ASTNode;
import ast.ASTVisitorException;

/**
 * CLASS ASTUtils
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class ASTUtils {

    public static void error(ASTNode node, String message)
            throws ASTVisitorException {
        throw new ASTVisitorException("[SemanticError] @line:" + node.getLine()
                + ", @column:" + node.getColumn()
                + " #" + message
                + "\n[Metafox-Interpreter]: Can't recover from previous error(s)");
    }

    public static void fatalError(ASTNode node, String errorCode)
            throws ASTVisitorException {
        throw new ASTVisitorException("[FatalError: #" + errorCode
                + "] Meta-Fox interpreter crashed @line:" + node.getLine()
                + ", @column:" + node.getColumn());
    }
}
