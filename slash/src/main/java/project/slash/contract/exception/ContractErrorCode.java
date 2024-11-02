package project.slash.contract.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.slash.common.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public enum ContractErrorCode implements ErrorCode {
	NOT_FOUND_CONTRACT(HttpStatus.BAD_REQUEST, "존재하지 않는 계약입니다."),
	NOT_TERMINATE_CONTRACT(HttpStatus.BAD_REQUEST, "종료되지 않은 계약입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
