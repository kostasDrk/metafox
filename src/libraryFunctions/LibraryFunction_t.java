package libraryFunctions;

public enum LibraryFunction_t {

    PRINT("print", -1),
    PRINT_LN("println", -1),
    SQRT("sqrt", 1),
    COS("cos", 1),
    SIN("sin", 1),
    LEN("len", 1),
    KEYS("keys", 1),
    VALUES("values", 1),
    STR("str", 1),
    ISNULL("isNull", 1),
    ISUNDEFINED("isUndefined", 1),
    ISINTEGER("isInteger", 1),
    ISREAL("isReal", 1),
    ISSTRING("isString", 1),
    ISBOOLEAN("isBoolean", 1),
    ISARRAY("isArray", 1),
    ISFUNC("isFunc", 1),
    ISLIBFUNC("isLibFunc", 1),
    ISOBJECT("isObject", 1),
    ISAST("isAST", 1),
    //FACTORY("factory", -2),
    COPY("copy", 1),
    DIAGNOSE("diagnose", 3),
    ADDFIRST("addFirst", 2),
    ADDONEXITPOINTS("addOnExitPoints", 2),
    GETASOBJECT("getAsObject", 1),
    ITERATOR("iterator", -1),
    GETNEXTITEM("getNextItem", 1),
    GETPREVITEM("getPrevItem", 1),
    HASNEXTITEM("hasNextItem", 1),
    HASPREVITEM("hasPrevItem", 1),
    ISSTATEMENT("isStatement", 1),
    ISIFSTATEMENT("isIfStatement", 1),
    ISFORSTATEMENT("isForStatement", 1),
    ISWHILESTATEMENT("isWhileStatement", 1),
    ISRETURNSTATEMENT("isReturnStatement", 1),
    ISBREAKSTATEMENT("isBreakStatement", 1),
    ISCONTINUESTATEMENT("isContinueStatement", 1),
    ISFUNCTIONDEFINITION("isFunctionDefinition", 1),
    ISEXPRESSION("isExpression", 1),
    ISASSIGNMENTEXPRESSION("isAssignmentExpression", 1),
    ISBINARYEXPRESSION("isBinaryExpression", 1),
    ISUNARYEXPRESSION("isUnaryExpression", 1),
    ISMETASYNTAX("isMetaSyntax", 1),
    ISMETAEXECUTE("isMetaExecute", 1),
    ISMETARUN("isMetaRun", 1),
    ISMETAEVAL("isMetaEval", 1),
    ISMETATOTEXT("isMetaToText", 1),
    ISIDENTIFIER("isIdentifier", 1),
    ISMEMBER("isMember", 1),
    ISLVALUECALL("isLvalueCall", 1),
    ISEXTENDEDCALL("isExtendedCall", 1),
    ISANONYMOUSCALL("isAnonymousCall", 1),
    ISCALL("isCall", 1),
    ISOBJECTDEFINITION("isObjectDefinition", 1),
    ISARRAYDEFINITION("isArrayDefinition", 1),
    ISINTEGERLITERAL("isIntegerLiteral", 1),
    ISDOUBLELITERAL("isDoubleLiteral", 1),
    ISSTRINGLITERAL("isStringLiteral", 1),
    ISNULLLITERAL("isNullLiteral", 1),
    ISTRUELITERAL("isTrueLiteral", 1),
    ISFALSELITERAL("isFalseLiteral", 1),
    GETEXPRESSION("getExpression", 1),
    SETEXPRESSION("setExpression", 2);

    private final String _name;
    private final int _totalArgs;

    private LibraryFunction_t(String name, int totalArgs) {
        _name = name;
        _totalArgs = totalArgs;
    }

    @Override
    public String toString() {
        return _name;
    }

    public int totalArgs() {
        return _totalArgs;
    }

    public static int totalArgs(String func) {
        int totalArgs = 0;

        switch (func) {
            case "print":
                totalArgs = -1;
                break;
            case "println":
                totalArgs = -1;
                break;
            case "sqrt":
                totalArgs = 1;
                break;
            case "cos":
                totalArgs = 1;
                break;
            case "sin":
                totalArgs = 1;
                break;
            case "len":
                totalArgs = 1;
                break;
            case "keys":
                totalArgs = 1;
                break;
            case "values":
                totalArgs = 1;
                break;
            case "str":
                totalArgs = 1;
                break;
            case "isNull":
                totalArgs = 1;
                break;
            case "isUndefined":
                totalArgs = 1;
                break;
            case "isInteger":
                totalArgs = 1;
                break;
            case "isReal":
                totalArgs = 1;
                break;
            case "isString":
                totalArgs = 1;
                break;
            case "isBoolean":
                totalArgs = 1;
                break;
            case "isArray":
                totalArgs = 1;
                break;
            case "isFunc":
                totalArgs = 1;
                break;
            case "isLibFunc":
                totalArgs = 1;
                break;
            case "isObject":
                totalArgs = 1;
                break;
            case "isAST":
                totalArgs = 1;
                break;
//            case "factory":
//                totalArgs = -2;
//                break;
            case "copy":
                totalArgs = 1;
                break;
            case "diagnose":
                totalArgs = 3;
                break;
            case "addFirst":
                totalArgs = 2;
                break;
            case "addOnExitPoints":
                totalArgs = 2;
                break;
            case "getAsObject":
                totalArgs = 1;
                break;
            case "iterator":
                totalArgs = 1;
                break;
            case "getNextItem":
                totalArgs = 1;
                break;
            case "getPrevItem":
                totalArgs = 1;
                break;
            case "hasNextItem":
                totalArgs = 1;
                break;
            case "hasPrevItem":
                totalArgs = 1;
                break;
            case "isStatement":
                totalArgs = 1;
                break;
            case "isIfStatement":
                totalArgs = 1;
                break;
            case "isForStatement":
                totalArgs = 1;
                break;
            case "isWhileStatement":
                totalArgs = 1;
                break;
            case "isReturnStatement":
                totalArgs = 1;
                break;
            case "isBreakStatement":
                totalArgs = 1;
                break;
            case "isContinueStatement":
                totalArgs = 1;
                break;
            case "isFunctionDefinition":
                totalArgs = 1;
                break;
            case "isExpression":
                totalArgs = 1;
                break;
            case "isAssignmentExpression":
                totalArgs = 1;
                break;
            case "isBinaryExpression":
                totalArgs = 1;
                break;
            case "isUnaryExpression":
                totalArgs = 1;
                break;
            case "isMetaSyntax":
                totalArgs = 1;
                break;
            case "isMetaExecute":
                totalArgs = 1;
                break;
            case "isMetaRun":
                totalArgs = 1;
                break;
            case "isMetaEval":
                totalArgs = 1;
                break;
            case "isMetaToText":
                totalArgs = 1;
                break;
            case "isIdentifier":
                totalArgs = 1;
                break;
            case "isMember":
                totalArgs = 1;
                break;
            case "isLvalueCall":
                totalArgs = 1;
                break;
            case "isExtendedCall":
                totalArgs = 1;
                break;
            case "isAnonymousCall":
                totalArgs = 1;
                break;
            case "isCall":
                totalArgs = 1;
                break;
            case "isObjectDefinition":
                totalArgs = 1;
                break;
            case "isArrayDefinition":
                totalArgs = 1;
                break;
            case "isIntegerLiteral":
                totalArgs = 1;
                break;
            case "isDoubleLiteral":
                totalArgs = 1;
                break;
            case "isStringLiteral":
                totalArgs = 1;
                break;
            case "isNullLiteral":
                totalArgs = 1;
                break;
            case "isTrueLiteral":
                totalArgs = 1;
                break;
            case "isFalseLiteral":
                totalArgs = 1;
                break;
            case "getExpression":
                totalArgs = 1;
                break;
            case "setExpression":
                totalArgs = 1;
                break;
            default:
                //fatal error
                break;
        }

        return totalArgs;
    }

    public static boolean isLibraryFunction(String name) {
        return name.equals(LibraryFunction_t.PRINT.toString())
                || name.equals(LibraryFunction_t.SQRT.toString())
                || name.equals(LibraryFunction_t.COS.toString())
                || name.equals(LibraryFunction_t.SIN.toString())
                || name.equals(LibraryFunction_t.LEN.toString())
                || name.equals(LibraryFunction_t.KEYS.toString())
                || name.equals(LibraryFunction_t.VALUES.toString())
                || name.equals(LibraryFunction_t.DIAGNOSE.toString())
                || name.equals(LibraryFunction_t.STR.toString())
                || name.equals(LibraryFunction_t.ISNULL.toString())
                || name.equals(LibraryFunction_t.ISUNDEFINED.toString())
                || name.equals(LibraryFunction_t.ISINTEGER.toString())
                || name.equals(LibraryFunction_t.ISREAL.toString())
                || name.equals(LibraryFunction_t.ISSTRING.toString())
                || name.equals(LibraryFunction_t.ISBOOLEAN.toString())
                || name.equals(LibraryFunction_t.ISARRAY.toString())
                || name.equals(LibraryFunction_t.ISFUNC.toString())
                || name.equals(LibraryFunction_t.ISLIBFUNC.toString())
                || name.equals(LibraryFunction_t.ISOBJECT.toString())
                || name.equals(LibraryFunction_t.ISAST.toString())
                || name.equals(LibraryFunction_t.COPY.toString())
                || name.equals(LibraryFunction_t.ADDFIRST.toString())
                || name.equals(LibraryFunction_t.ADDONEXITPOINTS.toString())
                || name.equals(LibraryFunction_t.GETASOBJECT.toString())
                || name.equals(LibraryFunction_t.ITERATOR.toString())
                || name.equals(LibraryFunction_t.GETNEXTITEM.toString())
                || name.equals(LibraryFunction_t.GETPREVITEM.toString())
                || name.equals(LibraryFunction_t.HASNEXTITEM.toString())
                || name.equals(LibraryFunction_t.HASPREVITEM.toString())
                || name.equals(LibraryFunction_t.ISSTATEMENT.toString())
                || name.equals(LibraryFunction_t.ISIFSTATEMENT.toString())
                || name.equals(LibraryFunction_t.ISFORSTATEMENT.toString())
                || name.equals(LibraryFunction_t.ISWHILESTATEMENT.toString())
                || name.equals(LibraryFunction_t.ISRETURNSTATEMENT.toString())
                || name.equals(LibraryFunction_t.ISBREAKSTATEMENT.toString())
                || name.equals(LibraryFunction_t.ISCONTINUESTATEMENT.toString())
                || name.equals(LibraryFunction_t.ISFUNCTIONDEFINITION.toString())
                || name.equals(LibraryFunction_t.ISEXPRESSION.toString())
                || name.equals(LibraryFunction_t.ISASSIGNMENTEXPRESSION.toString())
                || name.equals(LibraryFunction_t.ISBINARYEXPRESSION.toString())
                || name.equals(LibraryFunction_t.ISUNARYEXPRESSION.toString())
                || name.equals(LibraryFunction_t.ISMETASYNTAX.toString())
                || name.equals(LibraryFunction_t.ISMETAEXECUTE.toString())
                || name.equals(LibraryFunction_t.ISMETARUN.toString())
                || name.equals(LibraryFunction_t.ISMETAEVAL.toString())
                || name.equals(LibraryFunction_t.ISMETATOTEXT.toString())
                || name.equals(LibraryFunction_t.ISIDENTIFIER.toString())
                || name.equals(LibraryFunction_t.ISMEMBER.toString())
                || name.equals(LibraryFunction_t.ISLVALUECALL.toString())
                || name.equals(LibraryFunction_t.ISEXTENDEDCALL.toString())
                || name.equals(LibraryFunction_t.ISANONYMOUSCALL.toString())
                || name.equals(LibraryFunction_t.ISCALL.toString())
                || name.equals(LibraryFunction_t.ISOBJECTDEFINITION.toString())
                || name.equals(LibraryFunction_t.ISARRAYDEFINITION.toString())
                || name.equals(LibraryFunction_t.ISINTEGERLITERAL.toString())
                || name.equals(LibraryFunction_t.ISDOUBLELITERAL.toString())
                || name.equals(LibraryFunction_t.ISSTRINGLITERAL.toString())
                || name.equals(LibraryFunction_t.ISNULLLITERAL.toString())
                || name.equals(LibraryFunction_t.ISTRUELITERAL.toString())
                || name.equals(LibraryFunction_t.ISFALSELITERAL.toString())
                || name.equals(LibraryFunction_t.GETEXPRESSION.toString())
                || name.equals(LibraryFunction_t.SETEXPRESSION.toString());
    }

}
