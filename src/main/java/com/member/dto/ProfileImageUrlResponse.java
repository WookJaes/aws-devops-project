package com.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileImageUrlResponse {

	private final String url;
	private final String expiresAt;
}