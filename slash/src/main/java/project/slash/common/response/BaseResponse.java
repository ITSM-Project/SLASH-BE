package project.slash.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
}
