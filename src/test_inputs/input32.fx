function factory(){
    if(len(arguments) % 2 != 0){
        println("factory requires an even # of arguments");
        return;
    }
    objectAST = .<{}>.;
   
  for(i=0; i < len(arguments); i = i+2){
      key = arguments[i];
      value = arguments[i+1];
      addField(objectAST, key, value);
  }
    h = {
        obj : .!objectAST,
        new : (function foo(s){
                    return copy(s.obj);
                })
    };
    return h;
}


s = factory(.<Name>., .<"Anna">., .<"lastName">., .<"kokolaki">., .<"age">., .<23>., .<{"hello":"anna"}>., .<"hy540">.);
//s = factory("Name", "Anna", "lastName", "kokolaki");

b = s..new();
println(b);
