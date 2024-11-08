package project.slash.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.slash.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인가되지 않은 사용자");

	private final HttpStatus httpStatus;
	private final String message;
}
