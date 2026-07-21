package com.example.library.service;

import com.example.library.dao.entity.Member;
import com.example.library.dao.repository.MemberRepository;
import com.example.library.dto.MemberRequestDto;
import com.example.library.dto.MemberResponseDto;
import com.example.library.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberMapper::mapToDto)
                .toList();
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        return MemberMapper.mapToDto(member);
    }

    public MemberResponseDto createMember(MemberRequestDto dto) {
        Member saved = memberRepository.save(MemberMapper.mapToEntity(dto));
        return MemberMapper.mapToDto(saved);
    }

}
