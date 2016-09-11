package interpreter;

import java.io.FileReader;

import ast.ASTNode;
import ast.Program;
import ast.ASTVisitor;
import ast.visitors.ExecutionASTVisitor;
import ast.visitors.ToStringASTVisitor;

import interpreter.parser.parser;
import interpreter.lexer.lexer;

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
                
                ASTVisitor executionASTVisitor = new ExecutionASTVisitor((Program)program);
                program.accept(executionASTVisitor);
                // System.out.println("\n\n**Execution ok**\n\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
