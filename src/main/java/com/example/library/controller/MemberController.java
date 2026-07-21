package com.example.library.controller;

import com.example.library.dto.MemberRequestDto;
import com.example.library.dto.MemberResponseDto;
import com.example.library.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public Page<MemberResponseDto> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return memberService.getAllMembers(page, size, sortBy, direction);
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
