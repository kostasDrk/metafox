import java.io.FileReader;
import ast.ASTNode;
import ast.ASTVisitor;

public class MetafoxCompiler {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage : Compiler <inputfile>");
        }
        else {
            try {
                MyLexer mlx = new MyLexer(new FileReader(args[0]));
                parser p = new parser( mlx );

                ASTNode compUnit = (ASTNode) p.parse().value;
                //ASTVisitor printVisitor = new PrintASTVisitor();
                System.out.println("Parse ok");
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
