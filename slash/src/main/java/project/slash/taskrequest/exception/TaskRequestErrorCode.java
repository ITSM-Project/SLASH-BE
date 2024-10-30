package project.slash.taskrequest.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.slash.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum TaskRequestErrorCode implements ErrorCode {
	NOT_FOUND_TASK_TYPE(BAD_REQUEST, "존재하지 않는 업무 유형"),
	NOT_FOUND_REQUEST(BAD_REQUEST, "존재하지 않는 요청"),

	CANNOT_DELETE_STARTED_TASK(BAD_REQUEST, "진행중인 요청은 삭제할 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
