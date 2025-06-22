package com.crmw.CRM_BE.controllers;

import com.crmw.CRM_BE.dto.AuthRequestDto;
import com.crmw.CRM_BE.dto.ChangePasswordDto;
import com.crmw.CRM_BE.dto.UserResponseDto;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.service.JWTService;
import com.crmw.CRM_BE.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

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

        Optional<UserResponseDto> user = userService.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
        response.put("username", authRequestDto.getUsername());
        response.put("token", jwtService.generateToken(authRequestDto.getUsername()));
        response.put("userId", user.get().getId());
        response.put("userRole", user.get().getRole());
        response.put("isActive", user.get().isIsActive());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/users/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto dto, Authentication authentication) {
        String username = authentication.getName();
        Optional<Users> optionalUser = userService.findUserByUsernameRaw(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        Users user = optionalUser.get();

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userService.save(user);
        return ResponseEntity.ok("Password changed successfully.");
    }


    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<UserResponseDto> users = userService.findAllByRole("USER", pageable);

        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserStatus(@PathVariable Integer id, @RequestParam boolean active) {

        boolean response = userService.updateUserStatus(id, active);

        return response ? ResponseEntity.ok("User status updated successfully.") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PostMapping("/admin/register-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerUserByAdmin(@RequestBody AuthRequestDto authRequestDto) {
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


}
