package com.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.member.dto.MemberCreateRequest;
import com.member.dto.MemberCreateResponse;
import com.member.entity.Member;
import com.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public MemberCreateResponse create(MemberCreateRequest request) {
		Member member = new Member(
			request.getName(),
			request.getAge(),
			request.getMbti()
		);

		Member savedMember = memberRepository.save(member);
		return MemberCreateResponse.from(savedMember);
	}

	@Transactional(readOnly = true)
	public MemberCreateResponse getMember(Long id) {
		Member member = memberRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 팀원입니다.")
		);

		return MemberCreateResponse.from(member);
	}
}