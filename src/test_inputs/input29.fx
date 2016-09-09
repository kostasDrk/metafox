function foo(){
    b + (1 + 2) + (3 + 4) + (5 + 9) - 92.3;
    for(i=0; i<10; i++){
	print("");
   }
   b + c + d + (e*f)/2.9 -(43%2) - 0;
   return true;
}

//s = foo();
println("===========");
it = iterator(foo);

function complex_bin(expr){
    if(!isBinaryExpression(expr)){
	return 1;
    }
    local leftExpr = getLeftExpression(expr);
    local units = complex_bin(leftExpr);
    local rightExpr = getRightExpression(expr);
    local units = units + complex_bin(rightExpr);
    return units;
}


while(it..hasNext()){
    cur_stmt = it..next();
    if(isBinaryExpression(cur_stmt)){
	units = complex_bin(cur_stmt);
        if(units > 3){
	    println("[!] Complex expression found ("+units+" operands) @line "+getLine(cur_stmt)+" : ");
	    println(cur_stmt);
	}
    }
}

//s = foo();
