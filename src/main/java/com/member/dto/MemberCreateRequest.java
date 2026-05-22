package com.member.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberCreateRequest {

	@NotBlank(message = "이름은 필수 입력값입니다.")
	private String name;

	@Min(value = 1, message = "나이는 1 이상이어야 합니다.")
	private int age;

	@NotBlank(message = "MBTI는 필수 입력값입니다.")
	@Pattern(
		regexp = "^(INTJ|INTP|ENTJ|ENTP|INFJ|INFP|ENFJ|ENFP|ISTJ|ISFJ|ESTJ|ESFJ|ISTP|ISFP|ESTP|ESFP)$",
		message = "올바른 MBTI 형식이 아닙니다."
	)
	private String mbti;
}