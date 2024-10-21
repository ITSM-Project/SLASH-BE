package project.slash.system.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.slash.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum SystemsErrorCode implements ErrorCode {
	NOT_FOUND_EQUIPMENT(HttpStatus.BAD_REQUEST, "존재하지 않는 장비");

	private final HttpStatus httpStatus;
	private final String message;
}
