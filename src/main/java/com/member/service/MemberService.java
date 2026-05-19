package com.member.service;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.member.dto.MemberCreateRequest;
import com.member.dto.MemberResponse;
import com.member.dto.ProfileImageUrlResponse;
import com.member.entity.Member;
import com.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final S3Service s3Service;

	@Transactional
	public MemberResponse create(MemberCreateRequest request) {
		Member member = new Member(
			request.getName(),
			request.getAge(),
			request.getMbti()
		);

		Member savedMember = memberRepository.save(member);
		return MemberResponse.from(savedMember);
	}

	@Transactional(readOnly = true)
	public MemberResponse getMember(Long id) {
		Member member = findMember(id);
		return MemberResponse.from(member);
	}

	@Transactional
	public void uploadProfileImage(Long id, MultipartFile file) {
		log.info("[SERVICE - LOG] 프로필 이미지 업로드 시작 id = {}", id);

		Member member = findMember(id);
		String key = s3Service.upload(id, file);
		member.updateProfileImageKey(key);

		log.info("[SERVICE - LOG] 프로필 이미지 업로드 완료 id = {}, key = {}", id, key);
	}

	@Transactional(readOnly = true)
	public ProfileImageUrlResponse getProfileImage(Long id) {
		log.info("[SERVICE - LOG] 프로필 이미지 조회 시작 id = {}", id);

		Member member = findMember(id);

		if (member.getProfileImageKey() == null) {
			log.warn("[SERVICE - LOG] 프로필 이미지 없음 id = {}", id);
			throw new IllegalArgumentException("프로필 이미지가 없습니다.");
		}

		URL url = s3Service.getDownloadUrl(member.getProfileImageKey());
		Instant expiresAt = Instant.now().plus(Duration.ofDays(7));

		log.info("[SERVICE - LOG] Presigned URL 생성 완료 id = {}, expiresAt = {}", id, expiresAt);

		return new ProfileImageUrlResponse(url.toString(), expiresAt.toString());
	}

	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
	}
}