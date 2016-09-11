function foo(a, b, c){
    args = getFunctionArgs(foo);

    println(args);
    
    size = len(arguments);

    println("ARGS SIZE: ",size);


    for(i = 0; i < size; i++){
        println("arg#",i, " ",arguments[i]);
    }


    a = "ok";
    b = "45";
    c = "5.6";
    println();

    for(i = 0; i < size; i++){
        println("arg#",i, " ",arguments[i]);
    }

    arguments[0] = 5;

    println("arg#",0, " ", a);

    return;
}

foo(1,2,3,4);
