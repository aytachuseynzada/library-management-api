package com.example.library.service;

import com.example.library.dao.entity.Member;
import com.example.library.dao.repository.MemberRepository;
import com.example.library.dto.MemberResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}

