package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsersRepository iUsersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> usernameDb = iUsersRepository.findByUsername(username);

        Users user = usernameDb
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!Boolean.TRUE.equals(user.isIsactive())) {
            throw new UsernameNotFoundException("User is not active");
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
