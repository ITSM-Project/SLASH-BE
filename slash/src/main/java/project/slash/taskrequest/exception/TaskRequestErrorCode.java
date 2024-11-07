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

	CANNOT_DELETE_OR_EDIT_REQUEST(BAD_REQUEST, "수정, 삭제할 수 없는 요청입니다."),
	NOT_REQUEST_OWNER(BAD_REQUEST, "작성자가 아닙니다."),

	MANAGER_ALREADY_ASSIGNED(BAD_REQUEST,"담당 매니저가 존재하지않거나 접수완료 상태가 아닙니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
