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
echo "[+] Compilerception"
javac MetafoxCompiler.java
