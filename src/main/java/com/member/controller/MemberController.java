package com.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.member.dto.MemberCreateRequest;
import com.member.dto.MemberCreateResponse;
import com.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/api/members")
	public ResponseEntity<MemberCreateResponse> saveMember(@Valid @RequestBody MemberCreateRequest request) {
		log.info("[API - LOG] 팀원 저장 요청");
		return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request));
	}

	@GetMapping("/api/members/{id}")
	public ResponseEntity<MemberCreateResponse> getMember(@PathVariable Long id) {
		log.info("[API - LOG] 팀원 조회 요청 id = {}", id);
		return ResponseEntity.status(HttpStatus.OK).body(memberService.getMember(id));
	}
}