function f(self){
    println("Got it!");
    return;
}

function check_name(obj){
    if(!isNull(obj.name)){
	println("got a name..");
    }
}

stud = {name : "tom",
	lastName : "thompson",
	pref_f : check_name
};

pre = .< precond = self.pref_f;
	 if(!isNull(precond)) precond(self);
	>.;

diagnose(f, .<println("Entering");>., .<println("Exiting")>.);
//addFirst(f, pre);
//addLast(f, .<if(true) println("Exit func");>.);
f(stud);
