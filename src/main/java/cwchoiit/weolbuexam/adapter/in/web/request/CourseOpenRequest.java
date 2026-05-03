package cwchoiit.weolbuexam.adapter.in.web.request;

import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "강의 등록 요청")
public record CourseOpenRequest(
        @Schema(description = "강사 회원 ID") Long instructorId,
        @Schema(description = "강의명") String title,
        @Schema(description = "최대 수강 인원") Integer capacity,
        @Schema(description = "가격") Long price) {

    public CourseOpenPayload toPayload() {
        return new CourseOpenPayload(title, capacity, price);
    }
}
