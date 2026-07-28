package com.example.library.mapper;

import com.example.library.dao.entity.Role;
import com.example.library.dao.entity.User;
import com.example.library.dto.RegisterRequestDto;

public interface UserMapper {
    static User mapToEntity(RegisterRequestDto dto, String encodedPassword) {
        return User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .role(Role.USER)
                .build();
    }
}
