x = .<a + b>.;
a = 7;
b = 9;

y = .<.~x + 1>.;
z = .!.<.~x - .~y - 2>.;

//println(z);


function func(){
    println("yeah!");
}

func();
diagnose(func, "Entering func", "Exiting func");
func();
