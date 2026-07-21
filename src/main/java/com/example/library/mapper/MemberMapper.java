package com.example.library.mapper;

import com.example.library.dao.entity.Member;
import com.example.library.dto.MemberRequestDto;
import com.example.library.dto.MemberResponseDto;

public interface MemberMapper {
    static Member mapToEntity(MemberRequestDto dto) {
        return Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
    static MemberResponseDto mapToDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
    static void updateEntity(Member member, MemberRequestDto dto) {
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
    }
}
