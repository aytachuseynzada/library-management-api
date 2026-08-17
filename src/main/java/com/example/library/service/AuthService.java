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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

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

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        String role = authentication.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");

        String token = jwtService.generateToken(dto.getUsername(), role);

        return new AuthResponseDto(token);
    }
}