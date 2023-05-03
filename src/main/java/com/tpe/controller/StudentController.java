package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDto;
import com.tpe.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students") //http://localhost:8080/students
public class StudentController {

    //!!! Logger (slf4j)
    Logger logger =LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;


    //!!! Bütün ögrencileri getirelim
    @GetMapping() //http://localhost:8080/students + GET
    @PreAuthorize("hasRole('ADMIN')") //Bu metoda sadece ADMIN yetkisi olan kullanicilar erisebilsin. hasRole, ADMIN'in önüne "ROLE_" ekini otomatik ekler
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
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student) {
        studentService.createStudent(student);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Student is created successfuly");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.CREATED); //201
    }


    //request param / path variable
    //coklu sorguda RequestParam, tek sorguda PathVariable kullanilir-
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
    @GetMapping("{id}") //http://localhost:8080/students/1
    public ResponseEntity<Student> getStudentWithPath(@PathVariable("id") Long id) {

        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student);
    }


    //!!!Delete Student with id
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") Long id) { //Birden fazla data alinacaksa @PathVariable("id") yazilmali

        studentService.deleteStudent(id);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Student is deleted successfuly");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.OK); // return ResponseEntity.ok(map);
    }

    //!!! Update Student  --> Gerekli olanlar endPoint + id + güncellenecek bilgiler (PathVariable(json))
    @PutMapping("{id}") //http://localhost:8080/students/1
    public ResponseEntity<Map<String, String>> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) { //tek data alinacaksa @PathVariable long id seklinde parantez icinde id yazilmasa da olur.

        studentService.updateStudent(id, studentDto);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Student is updated successfuly");
        map.put("status", "true");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //!!! pageable
    @GetMapping("/page") //http://localhost:8080/students/page
    public ResponseEntity<Page<Student>> getAllWithPage(
            @RequestParam("page") int page, //kacinci sayfa gelsin
            @RequestParam("size") int size,  //sayfa basi kac ürün gelsin
            @RequestParam("sort") String prop, //hangi field'e göre siralanacak  --> opsiyoneldir
            @RequestParam("direction") Sort.Direction direction  //siralama türü  --> opsiyoneldir
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<Student> studentPage = studentService.getAllWithPage(pageable);

        return ResponseEntity.ok(studentPage);
    }


    //!!! Get By LastName
    @GetMapping("/querylastname")
    public ResponseEntity<List<Student>> getStudentByLastName(@RequestParam("lastName") String lastName) {

        List<Student> list = studentService.findStudent(lastName);

        return ResponseEntity.ok(list);
    }

    // !!! get All Student By Grade ( JPQL )
    @GetMapping("/grade/{grade}") // http://localhost:8080/students/grade/75  + GET
    public ResponseEntity<List<Student>> getStudentsEqualsGrade(@PathVariable("grade") Integer grade) {

        List<Student> list = studentService.findAllEqualsGrade(grade);

        return ResponseEntity.ok(list);
    }

    // !!! DB den direk DTO olarak datami almak istersem ??
    @GetMapping("/query/dto") // http://localhost:8080/students/query/dto?id=1   + GET
    public ResponseEntity<StudentDto> getStudentDTO(@RequestParam("id") Long id) {
        StudentDto studentDTO = studentService.findStudentDTOById(id);

        return  ResponseEntity.ok(studentDTO);

    }


    //!!! view
    @GetMapping("/welcome") //http://localhost:8080/students/welcome + GET
    public String welcome(HttpServletRequest request){

        logger.warn("---------------------------- Welcome{}", request.getServletPath());

        return "Welcome to Student Controller";
    }


}


