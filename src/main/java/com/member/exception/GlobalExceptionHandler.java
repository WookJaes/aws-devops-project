package com.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * 전역 예외 처리를 담당하는 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * IllegalArgumentException 예외를 처리한다.
	 *
	 * @param e 발생한 예외
	 * @return 예외 메시지 응답
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("[ERROR] 예외 발생", e);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	/**
	 * 요청 데이터의 유효성 검사 실패 예외를 처리한다.
	 *
	 * @param e 발생한 예외
	 * @return 유효성 검사 실패 메시지
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
		log.error("[ERROR] 유효성 검사 실패", e);

		String errorMessage = e.getBindingResult()
			.getFieldError() != null
			? e.getBindingResult().getFieldError().getDefaultMessage() : "유효성 검사 실패";

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}

	/**
	 * 처리되지 않은 서버 예외를 처리한다.
	 *
	 * @param e 발생한 예외
	 * @return 서버 오류 메시지
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		log.error("[ERROR] 서버 예외 발생", e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	}
}