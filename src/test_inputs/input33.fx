function foo(){
    x = 5 + 3;
    ++b + --c + d + (e*f)/2.9 -(43%2) - 0;
    x++;
}

function takeOp(prev){

    if(prev == "+"){
        return "-";
    
    }else if(prev == "-"){
        return "+";

    }else if(prev == "*"){
        return "/";

    }else if(prev == "/"){
        return "*";

    }else if(prev == "++"){
        return "--";

    }else if (prev == "--"){
        return "++";

    }else{
        return null;
    }
}


function changeOp(expr){
     if(isBinaryExpression(expr)){
        local leftExpr = getLeftExpression(expr);
        local units = changeOp(leftExpr);
        op = getOperator(expr);

        newOp = takeOp(op);
        if(newOp != null){
            setOperator(expr, newOp);
        }

        local rightExpr = getRightExpression(expr);

        changeOp(rightExpr);
    }else if(isUnaryExpression(expr)){
        op = getOperator(expr);

        newOp = takeOp(op);
        if(newOp != null){
            setOperator(expr, newOp);
        }
    }else
        return;
}


function traverse(a){
    it = iterator(a);
    while( it..hasNext()){
    	curItem = it..next();
        
        if(isBinaryExpression(curItem)){

            changeOp(curItem);
            
        }
    }

}



traverse(foo);

println(foo);








