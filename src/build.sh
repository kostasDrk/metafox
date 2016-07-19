echo "[+] Lex analysis"
jflex lexAnalysis.jflex

echo "[+] Generating grammar parser"
java -jar lib/java-cup-11a.jar grammar.cup

echo "[+] Compiling sym.java"
javac sym.java

echo "[+] Compiling parser.java"
javac parser.java

echo "[+] Compiling Lexer"
javac MyLexer.java

echo "[+] Compiling AST package"
javac ast/*.java

echo "[+] Compiling SymTable"
javac symbolTable/libraryFunctions/*.java
javac symbolTable/entries/*.java
javac symbolTable/*.java

echo "[+] Compiling ASTVisitor"
javac PrintASTVisitor.java

echo "[+] Compilerception"
javac MetafoxCompiler.java
