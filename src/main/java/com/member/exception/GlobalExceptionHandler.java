package com.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("[ERROR] 예외 발생", e);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
		log.error("[ERROR] 유효성 검사 실패", e);

		String errorMessage = e.getBindingResult()
			.getFieldError() != null
			? e.getBindingResult().getFieldError().getDefaultMessage()
			: "유효성 검사 실패";

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(errorMessage);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		log.error("[ERROR] 서버 예외 발생", e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body("서버 오류가 발생했습니다.");
	}
}