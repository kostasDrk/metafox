/*
a = 0.5;
b = [5,4];

for(i = 0; i< a -2 + b[0] + (4*5)/6 -(7%8) + 9; i++){
    
    println(i);    
}


for(i = 0, j = 0; i< 4 ; i++){
    local l = j ;
    if(i == 30 ){
        j++;
    }
    println(i);    
}*/

for(i = -1; i> -5 ; i--){
    local x;
    x = 10 + i;
    println(i);
    i = i- 2;
}

function isStaticVal(item){
    if(!isBinaryExpression(item)){
        if(isIntegerLiteral(item) or isDoubleLiteral(item)){
            return true; 
        }else{
            return false;
        }
    }

    local lexpr = getLeftExpression(item);

    lRetVal = isStaticVal(lexpr);
    if(!lRetVal)
        return false;


    local rexpr = getRightExpression(item);

    rRetVal = isStaticVal(rexpr);

    if(!rRetVal)
        return false;

    return true;
}


function checkExpr(id ,it){
    while(it..hasNext()){
        item = it..next();
        //println(getAstType(item));
        
        if(isStatement(item)){
            if(!checkExpr(id, iterator(item)))
                return false;
        }

        if(isAssignmentExpression(item)){
            lvalue = getIdentifier(getLvalue(item));
            if(lvalue == id){
                println("Loop not unroll, '", id, "' is lvalue in AssignmentExpression. ", item);
                return false;
            }
        }else if(isUnaryExpression(item)){
            lvalue = getIdentifier(getLvalue(item));
            op = getOperator(item);

            if( lvalue == id and ((op == "++") or (op == "--"))){
                println("Loop not unroll, '", id, "' is lvalue in UnaryExpression.", item);
                return false;
            }
        }
    }

    return true;
}

function unroll(id, it, newVal){
    while(it..hasNext()){
        item = it..next();

        if(isStatement(item)){
            unroll(id, iterator(item), newVal);
        }
        if(isLvalueCall(item)){
            args = getCallArguments(item);
            newArgs = [];
            for(_i=0; _i<len(args); _i++){
                tempArg = args[_i];
                println("tempArg: "+tempArg);
                if(getIdentifier(args[_i]) == id){
                    println("!!!!!");

                    tempArg = .@(str(newVal)+";");
                    println("TYPE::::");
                    println(getAstType(tempArg));
                    tempArg = getExpression(tempArg);
                    println("=====");
                }
                push(newArgs, tempArg);
                println("Type: "+getFoxType(tempArg));
            }
            setCallArguments(item, newArgs);
        }
    }
}


function traverse(stmt){
    it = iterator(stmt);
    while(it..hasNext()){
        item = it..next();

        if(isForStatement(item)){
            traverse(item);
            
            binExpr = getExpression(item);
            println(binExpr);
            leftExpr = getLeftExpression(binExpr);
            rightExpr = getRightExpression(binExpr);


            local leftIsStatic = isStaticVal(leftExpr);
            local rightIsStatic = isStaticVal(rightExpr);
            
            if((!leftIsStatic and !rightIsStatic) or (leftIsStatic and rightIsStatic)){
                println("1-Loop not unroll.");
                return;
            }

            local id;
            local idExpr;
            if(!leftIsStatic){
                println("left Not StaticVal");
                if(!isIdentifier(leftExpr)){
                    println("2a-Loop not unroll.");
                    return;
                }
                idExpr = leftExpr;
                id = getIdentifier(leftExpr); 
                
            }else{
                println("right Not StaticVal");
                if(!isIdentifier(rightExpr)){
                    println("2b-Loop not unroll.");
                    return;
                }
                idExpr = rightExpr;
                id = getIdentifier(rightExpr);
            }
            println("ID: ", id);

            if(checkExpr(id, iterator(item))){
                lExprList = getLeftExpressionList(item);
                rExprList = getRightExpressionList(item);


                for(f = 0 ; f< len(lExprList); f++){
                    .!lExprList[f];
                }

                while(.!binExpr){
                    //unroll(id, iterator(item), .!idExpr);
                    println(i);
                    for(f = 0 ; f< len(rExprList); f++){
                        .!rExprList[f];
                    }
                }
            }

         }

    }
}


traverse(PROGRAM);
