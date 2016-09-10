function foo(){
    if(true){
	println("yeah!");
    }else{
	println("nope");
    }
    println(123);
}


function traverse(stmt){
    it = iterator(stmt);
    while(it..hasNext()){
	curItem = it..next();
	if(isIfStatement(curItem)){
	    println("Replacing if..");
	    it..replace(.<println("replaced!");println("indeed!");>.);
//	    it..remove();
	}
    }
}

foo();
println("==========");
traverse(foo);
foo();
