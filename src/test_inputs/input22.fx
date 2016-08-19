
student = {name: "Kostas",
	   lastName: "Drk",
	   age: 23,
	   MO: 95.6};

for(i = 0; i < len(student); i++){
   key = keys(student)[i];
   println(key);
   student[key] = nil;
}

for(i = 0; i < len(student); i++){
   key = keys(student)[i];
   println(student[key]);
}
