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

echo "[+] Compiling Symbols package"
javac symbols/value/*.java

echo "[+] Compiling AST package"
javac ast/*.java

echo "[+] Compiling LibraryFunctions"
javac libraryFunctions/*.java

echo "[+] Compiling DataStructures"
javac dataStructures/*.java

echo "[+] Compiling Utils"
javac utils/*.java

echo "[+] Compiling Environment"
javac environment/*.java

echo "[+] Compilerception"
javac MetafoxCompiler.java
