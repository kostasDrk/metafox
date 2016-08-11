x = {
    "id": 5,
    "name": "maria",
    "sur": "fox"
};

y = [5, "maria", "fox"];


function f(){
    return {
    "id": 4,
    "name": "stelios",
    "sur": "foxakis"
};
}


function ftable(){
    return [5, "andreas", "foxidis"];
}


println(x.id);
println(x["id"]);

println(y[2]);

println(f().name);

println(f()["sur"]);

y[0] = y[1];

println(y[0]);

println(ftable()[2]);



