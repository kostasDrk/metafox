echo "[+] Generating Lexer"
jflex interpreter/lexer/lexAnalysis.jflex

echo "[+] Generating grammar parser"
java -jar lib/java-cup-11a.jar -destdir interpreter/parser -package interpreter.parser interpreter/parser/grammar.cup

echo "[+] Compiling sym.java"
javac interpreter/parser/sym.java

echo "[+] Compiling parser.java"
javac interpreter/parser/parser.java

echo "[+] Compiling Lexer"
javac interpreter/lexer/lexer.java

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
javac interpreter/MetafoxInterpreter.java
