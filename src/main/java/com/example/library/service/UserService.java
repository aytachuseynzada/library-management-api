package com.example.library.service;

import com.example.library.dao.entity.User;
import com.example.library.dao.repository.UserRepository;
import com.example.library.dto.RegisterRequestDto;
import com.example.library.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(RegisterRequestDto dto) {

        String encodedPassword =
                passwordEncoder.encode(dto.getPassword());

        User user = UserMapper.mapToEntity(dto, encodedPassword);

        return userRepository.save(user);
    }
}
