from = {
    city : "Hrakleion",
    Island : "Crete"
};


student = {
    name : "Nikos", 
    surname : "Papadakis",
    from : from,
    Age : 25

};

studentCopy = copy(student);

println(student);
println(studentCopy);


student.name = "makis";
studentCopy.from.city = "Athens";

println("=========");
println(student);
println(studentCopy);


studentCopy.Age = 50;
studentCopy.birth = "15/01/xxxx";
student.dateBirth = "10-08-xxxx";

courses = [352, 540, 562];
courses["number"] = 3;

student.courses = courses;

println("=========");
println(student);
println(studentCopy);

coursesCopy = copy(courses);
println(coursesCopy);



studentCopy.courses = coursesCopy;
studentCopy.courses = coursesCopy;
courses[len(courses)+5] = 567;
courses["number"] = studentCopy.courses["number"] + 1;

println();
println(student);
println(studentCopy);


println(studentCopy.courses["number"]);


table = [];

println(table);

table = {};

println(table);
