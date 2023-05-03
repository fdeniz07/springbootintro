package com.tpe.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Please provide FirstName")
    private String firstName;

    @NotBlank(message = "Please provide LastName")
    private String lastName;

    @NotBlank(message = "Please provide User Name")
    @Size(min =5, max=10, message = "Please provide a User Name min=5, max=10 chars long")
    private String userName;

    @NotBlank(message = "Please provide Password")
    private String password;
}
