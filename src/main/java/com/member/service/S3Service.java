package com.member.service;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

	private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);

	private final S3Template s3Template;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

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

	public URL getDownloadUrl(String key) {
		log.info("[S3 - LOG] Presigned URL 생성 시작 bucket = {}, key = {}", bucket, key);

		URL url = s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);

		log.info("[S3 - LOG] Presigned URL 생성 완료 key = {}, expiration = {}", key, PRESIGNED_URL_EXPIRATION);

		return url;
	}

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