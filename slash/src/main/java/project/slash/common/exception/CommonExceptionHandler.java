package project.slash.common.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import project.slash.common.response.BaseResponse;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<BaseResponse<Void>> handleBusinessException(BusinessException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(BaseResponse.error(e.getErrorCode().getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(BaseResponse.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse<Void>> handleInternalServerException(Exception e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(BaseResponse.error("INTERNAL_SERVER_ERROR"));
	}
}
