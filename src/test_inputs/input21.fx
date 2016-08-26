function power(a, N){
   if(N == 1){
	return a;
   }else{
	return .<a * power(a, N-1)>.;
   }
}

x = .<2>.;
s = .!power(x, 11);
println(s);

b = .<10 + k>.;
k = .<3 + .~l>.;
l = .<2>.;
s = .<.~b * .~b>.;
println(.!s);
