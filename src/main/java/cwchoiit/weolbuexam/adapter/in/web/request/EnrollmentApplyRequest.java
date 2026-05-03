package cwchoiit.weolbuexam.adapter.in.web.request;

import cwchoiit.weolbuexam.domain.enrollment.payload.EnrollmentApplyPayload;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "수강신청 요청")
public record EnrollmentApplyRequest(
        @Schema(description = "신청자 회원 ID") Long applicantId,
        @Schema(description = "신청할 강의 ID 목록") List<Long> courseIds) {

    public EnrollmentApplyPayload toPayload() {
        return new EnrollmentApplyPayload(courseIds);
    }
}
