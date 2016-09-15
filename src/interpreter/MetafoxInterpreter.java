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
package interpreter;

import java.io.FileReader;

import ast.ASTNode;
import ast.Program;
import ast.ASTVisitor;
import ast.visitors.ExecutionASTVisitor;
import ast.visitors.ToStringASTVisitor;

import interpreter.parser.parser;
import interpreter.lexer.lexer;

/**
 * CLASS MetafoxInterpreter
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class MetafoxInterpreter {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage : Compiler <inputfile>");
        } else {
            try {
                parser p = new parser(new lexer(new FileReader(args[0])));

                ASTNode program = (ASTNode) p.parse().value;

                //ASTVisitor printVisitor = new ToStringASTVisitor();
                //program.accept(printVisitor);
                //System.out.println(printVisitor.toString());
                //System.out.println("\n\n**Parse ok**\n\n");
                ASTVisitor executionASTVisitor = new ExecutionASTVisitor((Program) program);
                program.accept(executionASTVisitor);
                // System.out.println("\n\n**Execution ok**\n\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                // e.printStackTrace();
            }
        }
    }

}
