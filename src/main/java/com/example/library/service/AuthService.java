package com.example.library.service;

import com.example.library.dao.entity.User;
import com.example.library.dao.repository.UserRepository;
import com.example.library.dto.AuthResponseDto;
import com.example.library.dto.LoginRequestDto;
import com.example.library.dto.RegisterRequestDto;
import com.example.library.exception.UsernameAlreadyExistsException;
import com.example.library.mapper.UserMapper;
import com.example.library.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public void register(RegisterRequestDto dto){
        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        User user = UserMapper.mapToEntity(
                dto,
                passwordEncoder.encode(dto.getPassword())
        );

        userRepository.save(user);
    }


    public AuthResponseDto login(LoginRequestDto dto){

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(
                        () -> new BadCredentialsException("Invalid username or password")
                );

        System.out.println("Found user: " + user.getUsername());
        System.out.println("Stored hash: " + user.getPassword());
        System.out.println("Raw password entered: " + dto.getPassword());
        System.out.println("Matches: " + passwordEncoder.matches(dto.getPassword(), user.getPassword()));

        if(!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword()
        )){
            throw new BadCredentialsException("Invalid username or password");
        }


        String token =
                jwtService.generateToken(user.getUsername());


        return new AuthResponseDto(token);
    }
}
