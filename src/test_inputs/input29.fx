function foo(){
   b + (1 + 2) + (3 + 4) + (5 + 9) - 92.3;
   for(i=0; i<10; i++){
	print("");
   }
   b + c + d + (e*f)/2.9 -(43%2) - 0;
   if(!(a or (b or !s and l)) and s != d or (!b and c != false)){
	return false;
   }
   return true;
}

//s = foo();
println("===========");

function complex_bin(expr){
    if(!isBinaryExpression(expr)){
	if(isUnaryExpression(expr)){
	    units = complex_bin(getExpression(expr));
	    return units;
	}
//	print("[!] Terminal: ");
//	println(expr);
	return 1;
    }
    local leftExpr = getLeftExpression(expr);
    local units = complex_bin(leftExpr);
    local rightExpr = getRightExpression(expr);
    local units = units + complex_bin(rightExpr);
    return units;
}

it = iterator(foo);
while(it..hasNext()){
    curItem = it..next();
    if(isBinaryExpression(curItem)){
	units = complex_bin(curItem);
        if(units > 3){
	    println("[!] Complex expression found ("+units+" operands) @line "+getLine(curItem)+" : ");
	    println(curItem);
	}
    }else if(isIfStatement(curItem)){
	units = complex_bin(getExpression(curItem));
	if(units > 3){
	    println("[!] Complex if expression found ("+units+" operands) @line "+getLine(curItem)+" : ");
	    println(curItem);
	}
    }
}

//s = foo();
println("=======");
