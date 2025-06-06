package com.crmw.CRM_BE.controllers;

import com.crmw.CRM_BE.dto.AuthRequestDto;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.service.JWTService;
import com.crmw.CRM_BE.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequestDto authRequestDto) {
        String hashedPassword = passwordEncoder.encode(authRequestDto.getPassword());

        Users newUser = new Users();
        newUser.setUsername(authRequestDto.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setRole(authRequestDto.getRole());
        boolean result = userService.save(newUser);


        if (result) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User registration failed.");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> generateToken(@RequestBody AuthRequestDto authRequestDto) {
        String username = authRequestDto.getUsername();
        String password = authRequestDto.getPassword();


        Authentication authentication = userService.verify(authRequestDto);
        if (!authentication.isAuthenticated()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Incorrect User Credentials");

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Optional<Users> user = userService.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
            response.put("username", authRequestDto.getUsername());
            response.put("token", jwtService.generateToken(authRequestDto.getUsername()));
            response.put("userId", user.get().getId());
            response.put("userRole", user.get().getRole());
            return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
