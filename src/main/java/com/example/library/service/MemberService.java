package com.example.library.service;

import com.example.library.dao.entity.Member;
import com.example.library.dao.repository.MemberRepository;
import com.example.library.dto.MemberRequestDto;
import com.example.library.dto.MemberResponseDto;
import com.example.library.exception.MemberNotFoundException;
import com.example.library.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Page<MemberResponseDto> getAllMembers(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return memberRepository.findAllByDeletedFalse(pageable)
                .map(MemberMapper::mapToDto);
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));
        return MemberMapper.mapToDto(member);
    }

    public MemberResponseDto createMember(MemberRequestDto dto) {
        Member saved = memberRepository.save(MemberMapper.mapToEntity(dto));
        return MemberMapper.mapToDto(saved);
    }

    public MemberResponseDto updateMember(Long id, MemberRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));
        MemberMapper.updateEntity(member, dto);
        Member updated = memberRepository.save(member);
        return MemberMapper.mapToDto(updated);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new RuntimeException("Member not found with id: " + id));

        member.setDeleted(true);

        memberRepository.save(member);
    }

}
