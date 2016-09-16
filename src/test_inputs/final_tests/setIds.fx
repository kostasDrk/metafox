function traverse(stmt){
    it = iterator(stmt);
    while(it..hasNext()){
        curItem = it..next();
      
        // println(getAstType(curItem));
        if(isAssignmentExpression(curItem)){
      	  lvalue = getLvalue(curItem);
      	  if(isIdentifier(lvalue)){
      	      id = getIdentifier(lvalue);
      	      setIdentifierNew(lvalue, "_"+id);
      	  }
        }
        if(isFunctionDefinition(curItem)){
           id = getFunctionIdentifier(curItem);
           setIdentifier(id, "_"+getIdentifier(id));
        }
        if(isLvalueCall(curItem)){
           lvalue = getLvalue(curItem);
           if(isIdentifier(lvalue)){
              id = getIdentifier(lvalue);
              if(isLibFunc(id)){
                continue;
              }else{
                 type = getFoxType(id);
              }
              setIdentifier(lvalue, "_"+id);
           }
        }
    }
}

traverse(PROGRAM);
b = 9;
b;

function test(a , b, c){
    local b = 6;

    ::b = 10;
    println(b);
}


_b++;
// println(b);

println(_b);
println("_b: " + _b);

test(1, 2, 3);