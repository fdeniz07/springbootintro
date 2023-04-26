package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students") //http://localhost:8080/students
public class StudentController {

    @Autowired
    private StudentService studentService;


    //!!! Bütün ögrencileri getirelim
    @GetMapping() //http://localhost:8080/students + GET
    public ResponseEntity<List<Student>> getAll() {
        List<Student> students = studentService.getAll();

        return ResponseEntity.ok(students); //List<Student> + HTTP.Status code = 200
    }

    /*
    NOT:
    public ResponseEntity<List<Student>> getAll() { //Birden çok kişi geleceği için List<> yapısını kullandık.
                                                    Array kullanmamız için kişi sayısının belli olması gerekirdi.
                                                    ResponseEntity<> sayesinde hem Student'ları hem de Status Code'ları Repository'e göndermiş oluyoruz.
    */


    //!!! Create a new student
    @PostMapping  // http://localhost:8080/students/  + POST  + JSON
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student) {
        studentService.createStudent(student);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Student is created successfuly");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.CREATED); //201
    }


    //request param / path variable
    //cok sorgu request tek sorgu path
    //bir data path ile alindiginda // http://localhost:8080/students/1
    //request param okunakli, key+value
    //best practice tek data path variable cok data request param

    //!!! Get a Student by ID via RequestParam
    @GetMapping("/query") // http://localhost:8080/students/query?id=1
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id) {

        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student);
    }

    //!!! Get a Student by ID via PathVariable
    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentWithPath(@PathVariable("id") Long id) {

        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student);
    }


    //!!!Delete Student with id
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteStudent(@PathVariable("id") Long id){

        studentService.deleteStudent(id);


        Map<String,String> map = new HashMap<>();
        map.put("message", "Student is deleted successfuly");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.OK); // return ResponseEntity.ok(map);

    }


}


/*
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable @RequestParam(name = "id") int id){
        ...
    }

 */