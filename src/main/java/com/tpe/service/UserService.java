package com.tpe.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.domain.enums.UserRole;
import com.tpe.dto.UserRequest;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    public void saveUser(UserRequest userRequest) {

        //DTO --> POJO
        User myUser = new User();
        myUser.setFirstName(userRequest.getFirstName());
        myUser.setLastName(userRequest.getLastName());
        myUser.setUserName(userRequest.getUserName());

        //password sifrelenecek
        String password = userRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        myUser.setPassword(encodedPassword);

        //Role bilgisini setliyoruz, default degeri ADMIN olarak set'liyoruz.
        //Best Practice de böyle bir role var mi diye bak, yoksa exception firlatiriz
        Role role = roleService.getRoleType(UserRole.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        myUser.setRole(roles);

        userRepository.save(myUser);
    }
}
