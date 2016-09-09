b = { foo : function f(a){
		println("a = "+a);
	    }
    };

function bar(o){
    function foo(){
	return 1;
    }

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
    if(isMetaToText(cur_stmt)){
        println("[+] Expression: ");
        println(cur_stmt);
    }
}
