package project.slash.statistics.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.slash.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum StatisticsErrorCode implements ErrorCode {
	NOT_FOUND_STATISTICS(HttpStatus.BAD_REQUEST, "존재하지 않는 통계입니다."),
	STATISTICS_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 계산된 지표입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
