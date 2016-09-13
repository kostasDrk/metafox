
function complex_func(a, b){
	if(a = 2){
		println("'a' is equal to 2");
		return;
	}
	for(i = 0 ; i < 4; i++){
		if(i == 4){
			return i;
		}
		temp = 4;
		while(temp >= 3){
			if(false){
				println("You'll never reach me!");
			}
			return;
			temp--;
		}
	}
	return;
}

// Check if given item is a compound statement (has its own block)
function isCompoundStatement(item){
	if(isFunctionDefinition(item) or isIfStatement(item) or isForStatement(item) or isWhileStatement(item)){
		return true;
	}
	return false;
}

// Traverse and perform the specified checks on the given statement
function traverse(stmt){
	it = iterator(stmt);
	while(it..hasNext()){
		curItem = it..next();
		if(isCompoundStatement(curItem)){ // If compound statement
			condition = getExpression(curItem);	// Check its condition
			if(isAssignmentExpression(condition)){
				println("[!] Assignment in condition @line "+getLine(curItem));
			}
			if(isFalseLiteral(condition)){
				println("[!] Dead code found @line "+getLine(curItem));
			}else{
				traverse(curItem); // Recursively traverse the given compound statement
			}
		}
		if(isReturnStatement(curItem)){  // For return statements check if all return a value
			returnExpression = getExpression(curItem);
			if(returnExpression){
				::returnFlag = true;
			}else{
				if(::returnFlag) println("[!] Missing return value @line "+getLine(curItem));
				else push(::returns, curItem);
			}
			if(it..hasNext())
				println("[!] Dead code found @line "+getLine(curItem));
		}
	}
}

returns = [];
returnFlag = false;
traverse(complex_func);
if(!isEmpty(returns)){
	for(i = 0; i<len(returns); i++)
		println("[!] Missing return value @line "+getLine(returns[i]));
}