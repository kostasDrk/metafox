
function foo(self){
	println("foo: "+self.name);
}

function bar(ob){
	println("bar: "+ob.name);
}

function func(self, s, d){
	println("func: "+self.name);
}

function handleFunc(func, transformation){
	if(!isFunctionDefinition(func)){
		println("handleFunc requires a function definition AST");
		return;
	}

	// Create iterator to get functions first statement
	it = iterator(func);
	firstItem = it..next();
	if(firstItem) it..addBefore(transformation); // Apply wanted transformation to Function Definition AST
}

function traverse(stmt){
	// Set transformation to be made
	transformation =  .<precond = self.pref_f;
						if(precond) precond(self);>.;
	// Start iterating program AST
	it = iterator(stmt);
	while(it..hasNext()){
		curItem = it..next();
		if(isFunctionDefinition(curItem)){	// When a function definition is found
			args = getFunctionArgs(curItem); // get the arguments and
			if(args[0] == "self"){			//	check the first one
				handleFunc(curItem, transformation);  // Call handler function
			}
		}
	}
}

// Initial simple object
ob = {name : "just a var!"};
foo(ob);
bar(ob);
func(ob, 5, 9);

println("====");
// Traverse program to apply transformation
traverse(PROGRAM);

// Set pref_f field so preconditions in modified function can evaluate to true
ob.pref_f = (function (var){
	  				println("pref_f is set!");
	  			});
foo(ob);
bar(ob);
func(ob, 5, 9);