package com.crmw.CRM_BE.mapper;

import com.crmw.CRM_BE.dto.UserResponseDto;
import com.crmw.CRM_BE.entity.Users;

public class UserMapper {

    public static UserResponseDto toDto(Users user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setIsActive(user.isIsactive());
        return dto;
    }
    public static Users toEntity(UserResponseDto dto) {
        Users user = new Users();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        user.setCreatedAt(dto.getCreatedAt());
        user.setIsactive(dto.isIsActive());
        return user;
    }
}
