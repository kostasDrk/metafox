
function foo(a){
    if(s = 2){
        b = 999;
	return;
   } else {
	b = -999;
	return;
   }
   while(false){
	for(i = 0; i<10; i++){
	    i = i+1;
	    if(i == 10){
		return;
	    }
	}
	println("infinityyyyyyyyyyyyy");
   }
}

function traverse(stmt){
    if(!isStatement(stmt)){
	println("[!] traverse requires a statement AST");
    }
    it = iterator(stmt);
    while(it..hasNext()){
	curItem = it..next();
	if(isIfStatement(curItem) or isForStatement(curItem) or isWhileStatement(curItem)){
	    expr = getExpression(curItem);
	    if(isAssignmentExpression(expr)){
		println("[!] Assignment in condition found @line "+getLine(curItem));
	    }
	    if(isFalseLiteral(expr)){
		println("[!] Dead code @line "+getLine(curItem));
	    }
	    traverse(curItem);  // Recursively traverse the curItem
	}
	if(isReturnStatement(curItem)){
	    retExpr = getExpression(curItem);
	    if(!retExpr){
		println("[!] Missing return value @line "+getLine(curItem));
		println("[!] Fixing with value: -1");
		setExpression(curItem, .<-1>.);
	    }
	}
    }
}

println("=====");
traverse(foo);

println("=====");
traverse(foo);
