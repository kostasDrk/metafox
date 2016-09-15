package ast.utils;

import ast.ASTNode;
import ast.ASTVisitorException;

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
