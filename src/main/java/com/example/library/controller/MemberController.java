package com.example.library.controller;

import com.example.library.dto.MemberRequestDto;
import com.example.library.dto.MemberResponseDto;
import com.example.library.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(
        name = "Member",
        description = "Member management APIs"
)
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @Operation(
            summary = "Get all members",
            description = "Returns paginated and sorted list of members"
    )
    @GetMapping
    public Page<MemberResponseDto> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return memberService.getAllMembers(page, size, sortBy, direction);
    }
    @Operation(
            summary = "Get member by id",
            description = "Returns a single member by its identifier"
    )
    @GetMapping("/{id}")
    public MemberResponseDto getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }
    @Operation(
            summary = "Create new member",
            description = "Creates a new member and returns created data"
    )
    @PostMapping
    @ResponseStatus(CREATED)
    public MemberResponseDto createMember(@Valid @RequestBody MemberRequestDto dto) {
        return memberService.createMember(dto);
    }
    @Operation(
            summary = "Update member",
            description = "Updates existing member information"
    )
    @PutMapping("/{id}")
    public MemberResponseDto updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequestDto dto) {
        return memberService.updateMember(id, dto);
    }
    @Operation(
            summary = "Delete member",
            description = "Performs soft delete for a member"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}
