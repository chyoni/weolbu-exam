package cwchoiit.weolbuexam.adapter.in.web.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 API 응답")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        @Schema(description = "응답 데이터", nullable = true) T data,
        @Schema(description = "성공 여부") boolean success,
        @Schema(description = "에러 메시지", nullable = true) String errorMessage) {

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(null, true, null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, true, null);
    }

    public static ApiResponse<Void> failure(String errorMessage) {
        return new ApiResponse<>(null, false, errorMessage);
    }
}
