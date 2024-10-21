package project.slash.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import project.slash.common.response.BaseResponse;

@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<BaseResponse<Void>> handleBusinessException(BusinessException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(BaseResponse.error(e.getErrorCode().getMessage()));
	}
}
