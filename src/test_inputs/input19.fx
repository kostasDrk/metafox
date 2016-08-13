function f(b){
   b.x = 6;
   b["14"] = 2.4;
}

s = {x: 5, 1+7+6: "str!", z: 67.2, g: f};

s.g(s);
println("s.x:  "+ s["x"]);
println("s.y:  "+ s["14"]);
