package com.tpe.security.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService {

    //!!! Bu class'daki 1. amacimiz : Securitx katmanina User objelerini verip UserDetails türüne cevrilmesini saglamaktir. Kendi user'larimizi security katmanina tanitmis olacagiz
    //!!! Bu class'daki 2. amacimiz : Role bilgilerini Granted Authority'e cevirmek

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username).orElseThrow(() ->
                new ResourceNotFoundException("User not found with username : " + username));

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassword(),
                    buildGrantedAuthority(user.getRole())
            );
        } else {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }

    }

    private static List<SimpleGrantedAuthority> buildGrantedAuthority(final Set<Role> roles) { //final blogunu koymamizin nedeni Setin icerisini dolu gelmesidir.
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }

        return authorities;
    }
}
