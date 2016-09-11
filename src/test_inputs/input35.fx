function foo(){

    function bar(x , y){
        return x - 9 + y;
    }

    bar(1, 2);


    x = 5 + 3;
    b + c - d;
    x++;




    function test(a,b,c){
        return a + b + c;
    }

    test(1, 2, 3);


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

            //changeExp(curItem);
            ;
        }

        if(isFunctionDefinition(curItem)){
            println(getFunctionName(curItem));
            x = getFunctionArgs(curItem);

            if(contains(getFunctionName(curItem), "st")){
                for(i = 0; i< len(x); i++){
                    println("Arg#",i," ",x[i]);
                }
            }
        }
    }

}


traverse(foo);

//println(foo);
