
function foo(){
    for(i=0; i<10; i++){
	println(i);
    }
}

foo();
println("===========");

it = iterator(foo);

while(it..hasNext()){
    cur_stmt = it..next();
    if(isForStatement(cur_stmt)){
	expr = getExpression(cur_stmt);
	setExpression(cur_stmt, .<i < 20>.);
    }
}

foo();