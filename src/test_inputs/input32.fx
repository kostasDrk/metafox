function factory(pairs){
    if(len(pairs) % 2 != 0){
	println("factory requires an even # of arguments");
	return;
    }
    new = .<{}>.;
    for(i=0; i < len(pairs); i = i+2){
	key = pairs[i];
	value = pairs[i+1];
//	addField(new, key, value);
    }
    return new;
}

pairs = ["name", "", "lastName", "", "age", -1];
s = factory(pairs);
b = .!s;
if(isObject(b)) println(b);
