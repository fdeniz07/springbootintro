package com.tpe.repository;

import com.tpe.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //Opsiyonel
public interface StudentRepository extends JpaRepository<Student,Long> {

    boolean existsByEmail(String email);

    List<Student> findByLastName(String lastName);
}
