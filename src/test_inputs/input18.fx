function test(a, b){
	println(a * b);
	return test;
}

(function (x, y, z){
	a = x + y + z;

	println(sqrt(a));
	


	y = {"x":test};
	return y["x"];
})(1, 2, 3)(10000, 55555)(30, 50);





