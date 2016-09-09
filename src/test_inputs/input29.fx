function foo(){
    (1 + 2) + (3 + 4);
    for(i=0; i<10; i++){
	print("");
    }
    return true;
}

s = foo();
println("===========");

it = iterator(foo);

while(it..hasNext()){
    cur_stmt = it..next();
    if(isBinaryExpression(cur_stmt)){
	println(cur_stmt);
	leftExpr = getLeftExpression(cur_stmt);
	println(leftExpr);
	rightExpr = getRightExpression(cur_stmt);
	println(rightExpr);
	counter = 0;
	while(isBinaryExpression(leftExpr)){
	     println("=========== ["+counter+"]");
	     leftExpr = getLeftExpression(leftExpr);
	     println(leftExpr);
	     if(isBinaryExpression(rightExpr)){
		     rightExpr = getRightExpression(leftExpr);
		     println(rightExpr);
	     }
	     counter++;
	}
    }
}

s = foo();
