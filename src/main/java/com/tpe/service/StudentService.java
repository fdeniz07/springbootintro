package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDto;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public List<Student> getAll() {
        return studentRepository.findAll();
    }


    public void createStudent(Student student) {

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new ConflictException("Email is already exist!");
        }

        studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found with id : " + id));
    }

    public void deleteStudent(Long id) {
        Student student = findStudent(id);
        studentRepository.delete(student);
    }

    public void updateStudent(Long id, StudentDto studentDto) {
        //1- email db de var mi ??
        boolean emailExist = studentRepository.existsByEmail(studentDto.getEmail());

        //2- Bu id li student var mi? yoksa exception yolla
        Student student = findStudent(id);

        if (emailExist && !studentDto.getEmail().equals(student.getEmail())) {
            throw new ConflictException("Email is already exist");
        }

        /*
               senaryo 1 : kendi email mrc , mrc girdim         ( update olur )
            ** senaryo 2 : kendi email mrc, ahmt girdim ve DB de zaten var     ( exception )
               senaryo 3 : kendi email mrc, mhmt ve DB de yok     (update)
         */

        student.setName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setGrade(studentDto.getGrade());
        student.setEmail(studentDto.getEmail());
        student.setPhoneNumber(studentDto.getPhoneNumber());

        studentRepository.save(student);
    }

    public Page<Student> getAllWithPage(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public List<Student> findStudent(String lastName){
        return studentRepository.findByLastName(lastName);
    }
}
