package com.example.library.service;

import com.example.library.dao.entity.Member;
import com.example.library.dao.repository.MemberRepository;
import com.example.library.dto.MemberRequestDto;
import com.example.library.dto.MemberResponseDto;
import com.example.library.exception.MemberNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;


    @Test
    void shouldReturnMembersWithPagination() {

        Member member = new Member();
        member.setId(1L);
        member.setName("Ali");
        member.setEmail("ali@gmail.com");
        member.setDeleted(false);


        Page<Member> memberPage =
                new PageImpl<>(List.of(member));


        when(memberRepository.findAllByDeletedFalse(any(Pageable.class)))
                .thenReturn(memberPage);


        Page<MemberResponseDto> result =
                memberService.getAllMembers(
                        0,
                        10,
                        "id",
                        "asc"
                );


        assertEquals(1, result.getContent().size());
        assertEquals(
                "Ali",
                result.getContent().get(0).getName()
        );


        verify(memberRepository)
                .findAllByDeletedFalse(any(Pageable.class));
    }
    @Test
    void shouldReturnMemberById() {

        Member member = new Member();
        member.setId(1L);
        member.setName("Ali");
        member.setEmail("ali@gmail.com");

        when(memberRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(member));

        MemberResponseDto result = memberService.getMemberById(1L);

        assertEquals("Ali", result.getName());

        verify(memberRepository).findByIdAndDeletedFalse(1L);
    }
    @Test
    void shouldThrowMemberNotFoundExceptionWhenMemberDoesNotExist() {

        when(memberRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                MemberNotFoundException.class,
                () -> memberService.getMemberById(1L)
        );

        verify(memberRepository).findByIdAndDeletedFalse(1L);
    }
    @Test
    void shouldCreateMember() {

        MemberRequestDto dto = new MemberRequestDto();
        dto.setName("Ali");
        dto.setEmail("ali@gmail.com");

        Member savedMember = Member.builder()
                .id(1L)
                .name(dto.getName())
                .email(dto.getEmail())
                .build();

        when(memberRepository.save(any(Member.class)))
                .thenReturn(savedMember);

        MemberResponseDto result = memberService.createMember(dto);

        assertEquals("Ali", result.getName());

        verify(memberRepository).save(any(Member.class));
    }
    @Test
    void shouldUpdateMember() {

        Member member = new Member();
        member.setId(1L);
        member.setName("Old Name");
        member.setEmail("old@gmail.com");

        MemberRequestDto dto = new MemberRequestDto();
        dto.setName("New Name");
        dto.setEmail("new@gmail.com");

        when(memberRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(member));

        when(memberRepository.save(any(Member.class)))
                .thenReturn(member);

        MemberResponseDto result = memberService.updateMember(1L, dto);

        assertEquals("New Name", result.getName());

        verify(memberRepository).save(member);
    }
    @Test
    void shouldDeleteMember() {

        Member member = new Member();
        member.setId(1L);
        member.setDeleted(false);

        when(memberRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(member));

        memberService.deleteMember(1L);

        assertEquals(true, member.isDeleted());

        verify(memberRepository).save(member);
    }
    @Test
    void shouldThrowMemberNotFoundExceptionWhenDeletingNonExistingMember() {

        when(memberRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                MemberNotFoundException.class,
                () -> memberService.deleteMember(1L)
        );
    }
    @Test
    void shouldReturnMemberByEmail() {

        Member member = new Member();
        member.setId(1L);
        member.setName("Ali");
        member.setEmail("ali@gmail.com");

        when(memberRepository.findByEmailAndDeletedFalse("ali@gmail.com"))
                .thenReturn(Optional.of(member));

        MemberResponseDto result = memberService.getMemberByEmail("ali@gmail.com");

        assertEquals("Ali", result.getName());

        verify(memberRepository).findByEmailAndDeletedFalse("ali@gmail.com");
    }

    @Test
    void shouldThrowMemberNotFoundExceptionWhenEmailDoesNotExist() {

        when(memberRepository.findByEmailAndDeletedFalse("notfound@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                MemberNotFoundException.class,
                () -> memberService.getMemberByEmail("notfound@gmail.com")
        );
    }
}

