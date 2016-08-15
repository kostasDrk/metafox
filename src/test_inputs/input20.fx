
function f(b){
    println("sure.."+b.z);
}

s = {x:5, y:"str", z:87.4, g:f};
a = [1, 2, 3, 4, 6, 8, 2, 3, 4];

/*skeys = keys(s);

for(i=0; i<len(s); i++){
    println(skeys[i]); 
}*/

avalues = values(a);

for(i=0; i<len(a); i++){
    println(avalues[i]);
}
