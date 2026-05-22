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
import com.s3.dto.ProfileImageUrlResponse;
import com.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 팀원 관련 API 요청을 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	/**
	 * 팀원을 저장한다.
	 *
	 * @param request 팀원 생성 요청 데이터
	 * @return 저장된 팀원 정보
	 */
	@PostMapping
	public ResponseEntity<MemberResponse> saveMember(@Valid @RequestBody MemberCreateRequest request) {
		log.info("[API - LOG] 팀원 저장 요청");
		return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request));
	}

	/**
	 * 팀원 정보를 조회한다.
	 *
	 * @param id 팀원 ID
	 * @return 조회된 팀원 정보
	 */
	@GetMapping("/{id}")
	public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
		log.info("[API - LOG] 팀원 조회 요청 id = {}", id);
		return ResponseEntity.status(HttpStatus.OK).body(memberService.getMember(id));
	}

	/**
	 * 팀원의 프로필 이미지를 업로드한다.
	 *
	 * @param id 팀원 ID
	 * @param file 업로드할 이미지 파일
	 * @return 생성 응답
	 */
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

	/**
	 * 팀원의 프로필 이미지 URL을 조회한다.
	 *
	 * @param id 팀원 ID
	 * @return CloudFront URL 응답 데이터
	 */
	@GetMapping("/{id}/profile-image")
	public ResponseEntity<ProfileImageUrlResponse> getProfileImage(@PathVariable Long id) {
		log.info("[API - LOG] 프로필 이미지 조회 요청 id = {}", id);
		return ResponseEntity.status(HttpStatus.OK).body(memberService.getProfileImage(id));
	}
}