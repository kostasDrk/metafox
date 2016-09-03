function countStmts(foo){
   counter = 0;
   for(i = 0; i < len(stmtlist); i++){
	counter++;
	if(true){
	    a = 1+2;
	}
   }
   return counter;
}

function traverse(stmtObject){
     check = isObject(stmtObject);
     if(!check){
	println("traverse(object) requires an object");
	return;
     }
     stmts = stmtObject.statements;
     println("Entering '"+stmtObject.type+"' statement");
     if(stmtObject.type == "function") println("Func name: "+stmtObject.name);
     if(stmtObject.type == "if" and stmtObject.expression == true){
	     println("Expression: "+stmtObject.expression);
     }
     for(i=0; i<len(stmts); i++){
	if(isObject(stmts[i])){
	    traverse(stmts[i]);
	}
     }
     println("Exiting '"+stmtObject.type+"' statement");
}


funcObject = getAsObject(countStmts);

println(funcObject);
println("**********************");

traverse(funcObject);
