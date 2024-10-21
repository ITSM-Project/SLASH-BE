package project.slash.user.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.slash.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	NOT_FOUND_USER(BAD_REQUEST, "존재하지 않는 사용자");

	private final HttpStatus httpStatus;
	private final String message;
}
