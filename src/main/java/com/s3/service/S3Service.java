package com.s3.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * S3 파일 업로드 및 CloudFront URL 생성을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Template s3Template;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloudfront.domain}")
	private String cloudFrontDomain;

	/**
	 * 이미지를 S3에 업로드한다.
	 *
	 * @param memberId 팀원 ID
	 * @param file 업로드할 이미지 파일
	 * @return 저장된 S3 객체 key
	 */
	public String upload(Long memberId, MultipartFile file) {
		validateImageFile(file);

		try {
			String key = "uploads/" + memberId + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();

			log.info("[S3 - LOG] S3 업로드 시작 bucket = {}, key = {}, fileName = {}, size = {}",
				bucket, key, file.getOriginalFilename(), file.getSize());

			s3Template.upload(bucket, key, file.getInputStream());
			log.info("[S3 - LOG] S3 업로드 완료 bucket = {}, key = {}", bucket, key);

			return key;
		} catch (IOException e) {
			log.error("[S3 - LOG] S3 업로드 실패 memberId = {}, fileName = {}", memberId, file.getOriginalFilename(), e);
			throw new RuntimeException("파일 업로드 실패", e);
		}
	}

	/**
	 * CloudFront 접근 URL을 생성한다.
	 *
	 * @param key S3 객체 key
	 * @return CloudFront URL
	 */
	public String getCloudFrontUrl(String key) {
		log.info("[S3 - LOG] CloudFront URL 생성 시작 key = {}", key);

		String url = cloudFrontDomain.endsWith("/")
			? cloudFrontDomain + key : cloudFrontDomain + "/" + key;

		log.info("[S3 - LOG] CloudFront URL 생성 완료 key = {}, url = {}", key, url);
		return url;
	}

	/**
	 * 업로드 파일이 이미지인지 검증한다.
	 *
	 * @param file 업로드 파일
	 */
	private void validateImageFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			log.warn("[S3 - LOG] 업로드 파일 검증 실패 - 빈 파일");
			throw new IllegalArgumentException("파일이 비어 있습니다.");
		}

		String contentType = file.getContentType();

		if (contentType == null || !contentType.startsWith("image/")) {
			log.warn("[S3 - LOG] 업로드 파일 검증 실패 contentType = {}", contentType);
			throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
		}
	}
}