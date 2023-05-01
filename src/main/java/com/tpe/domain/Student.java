package com.tpe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //   @Setter(AccessLevel.NONE)
    //   @Getter
    private Long id;

    @NotNull(message = "first name can not be null")
    @NotBlank(message = "first name can not be white space")
    @Size(min = 2, max = 25, message = "First name '${validatedValue}' must be between {min} and {max} long")
    @Column(nullable = false, length = 25)
//    @Getter
//    @Setter
    private /*final*/ String name;

    @Column(nullable = false, length = 25)
    private /*final*/ String lastName;

    private /*final*/ Integer grade;

    @Column(nullable = false, unique = true)
    @Email(message = "Provide valid email")
    private /*final*/ String email;

    private /*final*/ String phoneNumber;

    //@Setter(AccessLevel.NONE)
    private LocalDateTime createDate = LocalDateTime.now();

    @OneToMany(mappedBy = "student")
    private List<Book> book = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
