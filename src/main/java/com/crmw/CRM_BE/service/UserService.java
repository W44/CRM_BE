package com.crmw.CRM_BE.service;


import com.crmw.CRM_BE.dto.AuthRequestDto;
import com.crmw.CRM_BE.dto.UserResponseDto;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.mapper.UserMapper;
import com.crmw.CRM_BE.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

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

    public Authentication verify(AuthRequestDto users) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
    }

    public Optional<UserResponseDto> findByUsername(String username) {
        return iUsersRepository.findByUsername(username)
                .map(UserMapper::toDto);
    }

    public Page<UserResponseDto> findAllByRole(String role, Pageable pageable) {
        return iUsersRepository.findByRole(role, pageable)
                .map(UserMapper::toDto);
    }

    public Optional<Users> findById(Integer id) {
        return iUsersRepository.findById(id);
    }

    public boolean updateUserStatus(Integer id,boolean active)
    {
         Optional<Users> userOptional = findById(id);
        if (userOptional.isEmpty()) {
            return false;
        }

        Users user =userOptional.get();
        user.setIsactive(active);
        save(user);
        return true;
    }
}
