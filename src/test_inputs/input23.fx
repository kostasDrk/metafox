
function func(a){
    println("yeah");
    for(i = 0; i < 4; i++){
	println("for"+i);
	if(i == 3){
	    s = 0;
	    while(s < 5){
		println("s = "+s);
		if(s >= 3){
		    return 8;
		}
		s++;
	    }
	    return 5;
	}
    }
}

enter = .<println("Entering")>.;
exit = .<println("Exiting")>.;

//diagnose(func, enter, exit);
addOnExitPoints(func, exit);
s = func(11);
println(s);
