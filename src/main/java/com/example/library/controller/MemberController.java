package com.example.library.controller;

import com.example.library.dto.MemberRequestDto;
import com.example.library.dto.MemberResponseDto;
import com.example.library.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public List<MemberResponseDto> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public MemberResponseDto getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public MemberResponseDto createMember(@Valid @RequestBody MemberRequestDto dto) {
        return memberService.createMember(dto);
    }

    @PutMapping("/{id}")
    public MemberResponseDto updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequestDto dto) {
        return memberService.updateMember(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}
