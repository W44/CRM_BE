package com.crmw.CRM_BE.service;


import com.crmw.CRM_BE.dto.AuthRequestDto;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public IUsersRepository iUsersRepository;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;


    public boolean save(Users users) {
        Users savedUser = iUsersRepository.save(users);
        return savedUser != null && savedUser.getId() != null;
    }

    public Authentication verify(AuthRequestDto users)
    {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
    }

    public Optional<Users> findByUsername(String username) {
        return iUsersRepository.findByUsername(username);
    }
}
