function factory(){
    if(len(arguments) % 2 != 0){
        println("factory requires an even # of arguments");
        return;
    }
    // Create new ObjectDefinition AST
  	objectAST = .<{}>.;
	 for(i=0; i < len(arguments); i = i+2){
      key = arguments[i];
      value = arguments[i+1];
      addField(objectAST, key, value); // Add given key-value pair to object AST
  	}

  	// Create a generator object
    gen = {
    	object : objectAST,
    	new : function (gen){
    		return .!gen.object;
    	}
    };
    return gen;
}

method = .<
		function (object){
					return object.name;
				}
		>.;
cfact = factory(.<"name">., .<"alex">., .<"lastName">., .<"burton">., .<"age">., .<19>., .<"grade">., .<8.3>., .<"getName">., method);

b = cfact..new();
c = cfact..new();
c.name = "george";

println(b..getName());
println(c..getName());