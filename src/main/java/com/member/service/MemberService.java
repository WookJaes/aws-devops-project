package com.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.member.dto.MemberCreateRequest;
import com.member.dto.MemberResponse;
import com.s3.dto.ProfileImageUrlResponse;
import com.member.entity.Member;
import com.member.repository.MemberRepository;
import com.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 팀원 관련 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final S3Service s3Service;

	/**
	 * 팀원을 생성한다.
	 *
	 * @param request 팀원 생성 요청 데이터
	 * @return 생성된 팀원 정보
	 */
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

	/**
	 * 팀원 정보를 조회한다.
	 *
	 * @param id 팀원 ID
	 * @return 조회된 팀원 정보
	 */
	@Transactional(readOnly = true)
	public MemberResponse getMember(Long id) {
		Member member = findMember(id);
		return MemberResponse.from(member);
	}

	/**
	 * 팀원의 프로필 이미지를 업로드한다.
	 *
	 * @param id 팀원 ID
	 * @param file 업로드할 이미지 파일
	 */
	@Transactional
	public void uploadProfileImage(Long id, MultipartFile file) {
		log.info("[SERVICE - LOG] 프로필 이미지 업로드 시작 id = {}", id);

		Member member = findMember(id);
		String key = s3Service.upload(id, file);
		member.updateProfileImageKey(key);

		log.info("[SERVICE - LOG] 프로필 이미지 업로드 완료 id = {}, key = {}", id, key);
	}

	/**
	 * 팀원의 프로필 이미지 URL을 조회한다.
	 *
	 * @param id 팀원 ID
	 * @return CloudFront URL 응답 데이터
	 */
	@Transactional(readOnly = true)
	public ProfileImageUrlResponse getProfileImage(Long id) {
		log.info("[SERVICE - LOG] 프로필 이미지 조회 시작 id = {}", id);

		Member member = findMember(id);

		if (member.getProfileImageKey() == null) {
			log.warn("[SERVICE - LOG] 프로필 이미지 없음 id = {}", id);
			throw new IllegalArgumentException("프로필 이미지가 없습니다.");
		}

		String url = s3Service.getCloudFrontUrl(member.getProfileImageKey());

		log.info("[SERVICE - LOG] CloudFront URL 생성 완료 id = {}", id);
		return new ProfileImageUrlResponse(url);
	}

	/**
	 * ID로 팀원을 조회한다.
	 *
	 * @param id 팀원 ID
	 * @return 조회된 팀원 엔티티
	 */
	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
	}
}