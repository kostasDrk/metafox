b = { foo : function f(a){
		println("a = "+a);
	    }
    };

function bar(o){
    function foo(){
	return 1;
    }
    5;
    6.94;
    "string";
    null;
    true;
    false;
    [1 , 3.21, "str", x];
    {x : 4, y : "str"};
    (function foo(abc){println(a);})(3);
    println(o);
    5+7;
    .#.<a+b>.;
    a = 5+9;
    a++;
    break;
    continue;
    if(true){
	println("yeah!");
    }
    while(a < 9){
	a--;
    }
    for(i=0; i<10; i++){
	println(i);
    }
    return true;
}


s = iterator(bar);
while(s..hasNext()){
    cur_stmt = s..next();
/*    if(isStatement(cur_stmt)){
	println("[+] Statement: ");
	println(cur_stmt);
    }*/
    if(isWhileStatement(cur_stmt)){
	expr = getExpression(cur_stmt);
        println("[+] Expression: ");
        println(expr);
	setExpression(cur_stmt, .<false>.);
	expr = getExpression(cur_stmt);
	println("[+] New expression: ");
	println(expr);
    }
}
