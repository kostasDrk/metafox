echo "Clean-up directory."

Clean_lexer_parser(){
    rm interpreter/parser/*.class
    rm interpreter/lexer/*.class
    rm interpreter/lexer/lexer.java*
    rm interpreter/parser/parser.java
    rm interpreter/parser/sym.java

}

Clean_Interpreter(){

    rm ast/*class
    rm ast/utils/*class
    rm ast/visitors/*class

    rm dataStructures/*.class

    rm environment/*class

    rm interpreter/*.class

    rm libraryFunctions/*class

    rm symbols/value/*.class

    rm utils/*class

}

if [ $# -eq 1  ]
then

    if [ $1 = "-all" ]
    then
        Clean_lexer_parser
        Clean_Interpreter
    else
        echo "Unrecognized option :"$1 
        echo "Legal options:"
        echo "1) ./clean"
        echo "2) ./clean -all"
    fi
elif [ $# -eq 0 ]
then
    Clean_Interpreter

else
    echo "Wrong number of arguments given."    
fi

