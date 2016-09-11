function factory(pairs){
    if(len(pairs) % 2 != 0){
    println("factory requires an even # of arguments");
    return;
    }
    new = .<{}>.;
   
    addFields(new, pairs);
    
    return new;
}

pairs = [.<Name>., .<"Anna">., .<"lastName">., .<"kokolaki">., .<"age">., .<23>., .<{"hello":"anna"}>., .<"hy540">.];
s = factory(pairs);
if(s) b = .!s;
if(isObject(b)) println(b);
