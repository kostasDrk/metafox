package ast;

import java.util.List;
// import org.objectweb.asm.Type;
import symbol.SymTable;
import symbol.SymTableEntry;

public class ASTUtils {

    public static final String SYMTABLE_PROPERTY = "SYMTABLE_PROPERTY";
    public static final String TYPE_PROPERTY = "TYPE_PROPERTY";

    private ASTUtils() {
    }

    @SuppressWarnings("unchecked")
    public static SymTable<SymTableEntry> getEnv(ASTNode node) {
        return (SymTable<SymTableEntry>) node.getProperty(SYMTABLE_PROPERTY);
    }

    @SuppressWarnings("unchecked")
    public static SymTable<SymTableEntry> getSafeEnv(ASTNode node)
            throws ASTVisitorException {
        SymTable<SymTableEntry> symTable = (SymTable<SymTableEntry>) node
                .getProperty(SYMTABLE_PROPERTY);
        if (symTable == null) {
            ASTUtils.error(node, "Symbol table not found.");
        }
        return symTable;
    }

    public static void setEnv(ASTNode node, SymTable<SymTableEntry> env) {
        node.setProperty(SYMTABLE_PROPERTY, env);
    }

    /*public static org.objectweb.asm.Type getType(ASTNode node) {
        return (org.objectweb.asm.Type) node.getProperty(TYPE_PROPERTY);
    }

    public static void setType(ASTNode node, org.objectweb.asm.Type type) {
        node.setProperty(TYPE_PROPERTY, type);
    }*/
    

    public static void error(ASTNode node, String message)
            throws ASTVisitorException {
        throw new ASTVisitorException(node.getLine() + ":" + node.getColumn()
                + ": " + message);
    }

}
