
function div(a, b){
	ret = a/b;
	println("a / b = "+a+" / "+b+" = "+ret);
	return ret;
}

function traverse(stmt){
	if(!isStatement(stmt)){
		println("traverse argument must be a valid statement AST");
		return;
	}

	onEnter = .<println("Entering function");>.;
	onExit = .<println("Exiting function");>.;
	curItem = null;
	stmt_counter = 0;
	
	// Traverse the given function AST
	it = iterator(stmt);
	while(it..hasNext()){
		curItem = it..next();
		if(stmt_counter == 0) it..addBefore(onEnter);  // Add given statement/statement list on entry
		stmt_counter++;
	}
	if(isReturnStatement(curItem))
		it..addBefore(onExit);	// Add given statement/statement list on exit
	else
		it..addAfter(onExit);	// Add given statement/statement list on exit
}

ret = div(10, 2);

println("\n=====\n");
traverse(div);

ret = div(10, 2);

/*
it = {
	iter : ast.visitor.IteratorASTVisitor@8f39ffff,
	next : function #ANONYMOUS#_0(iterator){
				return getNextItem(iterator.iter);
			},
	hasNext : function #ANONYMOUS#_1(iterator){
				return hasNextItem(iterator.iter);
			},
	remove : function #ANONYMOUS#_2(iterator){
				return removeItem(iterator.iter);
			}
	addBefore : function #ANONYMOUS#_3(iterator, newstmt){
				addItemBefore(iterator.iter, newstmt);
			}
	...
};
*/