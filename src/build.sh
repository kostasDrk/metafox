#!/bin/bash

# function to compile lexer and parser

Compile_lexer_parser(){

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
}


Compile_Interpreter(){
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

}


if [ $# -eq 1  ]
then

    if [ $1 = "-all" ]
    then
        Compile_lexer_parser
        Compile_Interpreter
    else
        echo "Unrecognized option :"$1 
        echo "Legal options:"
        echo "1) ./build"
        echo "2) ./build -all"
    fi
elif [ $# -eq 0 ]
then
    Compile_Interpreter

else
    echo "Wrong number of arguments given."    
fi
