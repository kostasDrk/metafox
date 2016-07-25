package ast.utils;

import ast.ASTNode;
import ast.ASTVisitorException;

public class ASTUtils {

    public static void error(ASTNode node, String message)
            throws ASTVisitorException {
        throw new ASTVisitorException("SemanticError @line:" + node.getLine()
                + ", @column:" + node.getColumn()
                + " #" + message);
    }
}
