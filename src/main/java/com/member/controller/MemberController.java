package com.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.member.dto.MemberCreateRequest;
import com.member.dto.MemberResponse;
import com.member.dto.ProfileImageUrlResponse;
import com.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<MemberResponse> saveMember(@Valid @RequestBody MemberCreateRequest request) {
		log.info("[API - LOG] 팀원 저장 요청");
		return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
		log.info("[API - LOG] 팀원 조회 요청 id = {}", id);
		return ResponseEntity.status(HttpStatus.OK).body(memberService.getMember(id));
	}

	@PostMapping("/{id}/profile-image")
	public ResponseEntity<Void> uploadProfileImage(
		@PathVariable Long id,
		@RequestParam("file") MultipartFile file
	) {
		log.info("[API - LOG] 프로필 이미지 업로드 요청 id = {}, fileName = {}, size = {}"
			, id, file.getOriginalFilename(), file.getSize());
		memberService.uploadProfileImage(id, file);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{id}/profile-image")
	public ResponseEntity<ProfileImageUrlResponse> getProfileImage(@PathVariable Long id) {
		log.info("[API - LOG] 프로필 이미지 조회 요청 id = {}", id);
		return ResponseEntity.status(HttpStatus.OK).body(memberService.getProfileImage(id));
	}
}