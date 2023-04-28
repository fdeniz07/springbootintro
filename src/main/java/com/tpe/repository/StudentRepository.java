package com.tpe.repository;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //Opsiyonel
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    List<Student> findByLastName(String lastName);


    // JPQL
    @Query("SELECT s FROM Student s WHERE s.grade=:pGrade")
    List<Student> findAllEqualsGrade(@Param("pGrade") Integer grade);

    // SQL
    @Query(value ="SELECT * FROM Student s WHERE s.grade=:pGrade" , nativeQuery = true)
    List<Student> findAllEqualsGradeWithSQL(@Param("pGrade") Integer grade);

    // JPQL mucizesi ile POJO-DTO donusumu
    @Query("SELECT new com.tpe.dto.StudentDto(s) FROM Student s WHERE s.id=:id")
    Optional<StudentDto> findStudentDTOById(@Param("id")Long id);
}
