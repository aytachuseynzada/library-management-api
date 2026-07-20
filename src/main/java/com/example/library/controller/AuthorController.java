package com.example.library.controller;

import com.example.library.service.AuthorService;
import com.example.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
}
