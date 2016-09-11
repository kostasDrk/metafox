function foo(){
    x = 5 + 3;
    b + c - d;
    x++;
}


function changeExp(expr){
    if(isBinaryExpression(expr)){
        local leftExpr = getLeftExpression(expr);
        local units = changeExp(leftExpr);
        op = getOperator(expr);

        setRightExpression(expr, .<x>.);

        local rightExpr = getRightExpression(expr);

        changeExp(rightExpr);
    }else
        return;
}


function traverse(a){
    it = iterator(a);
    while( it..hasNext()){
        curItem = it..next();

        if(isBinaryExpression(curItem)){

            changeExp(curItem);

        }
    }

}


traverse(foo);

println(foo);