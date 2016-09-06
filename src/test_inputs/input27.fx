local x = 5;

println(::x);


function tester(a, b, c){
    local a = 37;
    
    println(a);

    local x = 50;
    println("local x = ", x);
    println("::x =",::x);

    y = 10;


    if(y == 10){
        local y = 15; 
        println(y);
    }
}

tester(1,2,3);