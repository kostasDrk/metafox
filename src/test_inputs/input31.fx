
function foo(a){
   print(a, b, c);
   if(s = 2){
        b = 999;
	return;
   } else {
	b = -999;
	return;
	a = 1;
   }
   while(false){
	for(i = 0; i<10; i++){
	    i = i+1;
	}
	println("infinityyyyyyyyyyyyy");
   }
   return 3;
}

function traverse(stmt){
    if(!isStatement(stmt)){
	println("[!] traverse requires a statement AST");
    }
    it = iterator(stmt);
    lastItem = null;
    while(it..hasNext()){
	curItem = it..next();
	if(isLvalueCall(curItem)){
	    setLvalue(curItem, .<print>.);
	}
	if(isIfStatement(curItem)){
	    //it..replace(.<println("Replaced!"); println("Yes, indeed!");>.);
	    it..addBefore(.<println("Entering if...");>.);
	}
	if(isIfStatement(curItem) or isForStatement(curItem) or isWhileStatement(curItem)){
	    expr = getExpression(curItem);
	    if(isAssignmentExpression(expr)){
		println("[!] Assignment in condition found @line "+getLine(curItem));
	    }
	    if(isFalseLiteral(expr)){
		println("[!] Dead code @line "+getLine(curItem));
	    }
	    traverse(curItem);  // Recursively traverse the curItem
	    if(isIfStatement(curItem)){
	         traverse(getElseStatement(curItem));
	    }
	}
//	if(isForStatement(curItem)) it..remove();
	if(isReturnStatement(curItem)){
	    retExpr = getExpression(curItem);
	    if(!retExpr){
		println("[!] Missing return value @line "+getLine(curItem));
		println("[!] Fixing with value: -1");
		setExpression(curItem, .<-1>.);
	    }
	    if(it..hasNext()){		
		println("[!] Dead code @line "+getLine(curItem));
	    }
	}
	lastItem = curItem;
    }
    if(isFunctionDefinition(stmt)){
	exit = .<println("Exiting foo..");>.;
	if(isReturnStatement(lastItem)){
	     it..addBefore(exit);
	}else{
	     it..addAfter(exit);
	}
    }
}

println("=====");
traverse(foo);

println("=====");
//traverse(foo);
println(foo);
//l = foo(true);
//println(l);
