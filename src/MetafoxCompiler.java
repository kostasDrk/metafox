
import java.io.FileReader;
import ast.ASTNode;
import ast.ASTVisitor;

public class MetafoxCompiler {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage : Compiler <inputfile>");
        } else {
            try {
                parser p = new parser(new MyLexer(new FileReader(args[0])));

                ASTNode program = (ASTNode) p.parse().value;

                ASTVisitor printVisitor = new PrintASTVisitor();
                program.accept(printVisitor);
                System.out.println("\n\n**Parse ok**\n\n");

                ASTVisitor executionASTVisitor = new ExecutionASTVisitor();
                program.accept(executionASTVisitor);
                System.out.println("\n\n**Execution ok**\n\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
