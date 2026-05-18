package com.member.dto;

import com.member.entity.Member;

import lombok.Getter;

@Getter
public class MemberCreateResponse {

	private final Long id;
	private final String name;
	private final int age;
	private final String mbti;

	public MemberCreateResponse(Long id, String name, int age, String mbti) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.mbti = mbti;
	}

	public static MemberCreateResponse from(Member member) {
		return new MemberCreateResponse(
			member.getId(),
			member.getName(),
			member.getAge(),
			member.getMbti()
		);
	}
}