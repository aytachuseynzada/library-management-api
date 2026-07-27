package com.example.library.service;

import com.example.library.dao.entity.User;
import com.example.library.dao.repository.UserRepository;
import com.example.library.dto.AuthResponseDto;
import com.example.library.dto.LoginRequestDto;
import com.example.library.dto.RegisterRequestDto;
import com.example.library.mapper.UserMapper;
import com.example.library.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public void register(RegisterRequestDto dto){

        User user = UserMapper.mapToEntity(
                dto,
                passwordEncoder.encode(dto.getPassword())
        );

        userRepository.save(user);
    }


    public AuthResponseDto login(LoginRequestDto dto){

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(
                        () -> new RuntimeException("Invalid credentials")
                );


        if(!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword()
        )){
            throw new RuntimeException("Invalid credentials");
        }


        String token =
                jwtService.generateToken(user.getUsername());


        return new AuthResponseDto(token);
    }
}
