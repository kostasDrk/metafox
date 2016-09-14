function complex_func(a, b){
	if(a = 2){
		println("'a' is equal to 2");
		return;
	}
	b + (1 + 2) + (3 + 4) + (5 + 9) - 92.3;
	for(i = 0 ; i < 4; i++){
		if(i == 4){
			return i;
		}
		temp = 4;
		while(temp >= 3){
			if(false){
				println("You'll never reach me!");
			}
			temp--;
		}
	}
	if(!(a or (b or !s and l)) and s != d or (!b and c != false)){
		return false;
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

// Calculate the total number of operands of a given expression
function handleBinaryExpression(expr){
	if(!isBinaryExpression(expr)){
		if(isUnaryExpression(expr)){
			units = handleBinaryExpression(getExpression(expr));
			return units;
		}
		return 1;
	}
	leftExpr = getLeftExpression(expr);
	units = handleBinaryExpression(leftExpr); // Recursively handle the left part of the expression
	rightExpr = getRightExpression(expr);
	units = units + handleBinaryExpression(rightExpr); // and afterwards the right part
	return units;
}

function traverse(stmt){
	size = 0;
	it = iterator(stmt);
	while(it..hasNext()){
		curItem = it..next();
		if(isCompoundStatement(curItem)){
			size = size + traverse(curItem); // recursively iterate over the compound statements and count their size
			units = handleBinaryExpression(getExpression(curItem)); // check for complex conditions
			if(units >= 4) println("[!] Complex binary expression in condition @line "+getLine(curItem)+" ("+units+" operands)");
		}
		if(isBinaryExpression(curItem)){
			units = handleBinaryExpression(curItem);
			if(units >= 4) println("[!] Complex binary expression @line "+getLine(curItem)+" ("+units+" operands)");
		}
		size++;
	}
	return size;
}

ret = traverse(complex_func);
if(ret >= 9){
	println("[!] complex_func is too large: "+ret+" statements found");
}