package project.slash.common.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import project.slash.taskrequest.dto.request.TaskResponseRequestDTO;

@JsonPropertyOrder({"success", "data", "message"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponse<T>(Boolean success, T data, String message) {

	public static <T> BaseResponse<T> ok(T data) {
		return new BaseResponse<>(true, data, null);
	}

	public static BaseResponse<Void> ok() {
		return new BaseResponse<>(true, null, null);
	}

	public static BaseResponse<Void> error(String message) {
		return new BaseResponse<>(false, null, message);
	}

	public static <T> BaseResponse<Map<String, Object>> ok(Map<String, Object> responseData) {
		return new BaseResponse<>(true, responseData, null);
	}

	public static BaseResponse<Map<String, Object>> ok(Object data, int totalPages) {
		Map<String, Object> responseData = Map.of(
			"results", data,
			"totalPages", totalPages
		);
		return new BaseResponse<>(true, responseData, null);
	}
}
