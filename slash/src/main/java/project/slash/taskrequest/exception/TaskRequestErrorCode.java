package project.slash.taskrequest.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.slash.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum TaskRequestErrorCode implements ErrorCode {
	NOT_FOUND_TASK_TYPE(BAD_REQUEST, "존재하지 않는 업무 유형");

	private final HttpStatus httpStatus;
	private final String message;
}
