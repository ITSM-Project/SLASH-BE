package project.slash.contract.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.slash.common.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public enum ContractErrorCode implements ErrorCode {
	NOT_FOUND_ITEMS(HttpStatus.BAD_REQUEST, "올바른 서비스 평가 항목이 아닙니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
