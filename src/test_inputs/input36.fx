
function foo(){
    println("Just a foo function");
}

function traverse(stmt){
    it = iterator(stmt);
    while(it..hasNext()){
	curItem = it..next();
	if(isFunctionDefinition(curItem)) println(getFunctionName(curItem));
	if(isLvalueCall(curItem)){
	    println(curItem);
	}
    }
}

traverse(PROGRAM);
