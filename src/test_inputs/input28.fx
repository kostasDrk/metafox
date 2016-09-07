b = { foo : function f(a){
		println("a = "+a);
	    }
    };

function bar(o){
    println(o);
    a = 5+9;
    if(true){
	println("yeah!");
    }
}


s = iterator(bar);
while(s..hasNext()){
    println(s..next());
}
