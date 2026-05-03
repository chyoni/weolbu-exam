package cwchoiit.weolbuexam.adapter.in.web.response;

import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.enrollment.Enrollment;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수강신청 결과")
public record EnrollmentApplyResponse(
        @Schema(description = "수강 ID") Long enrollmentId,
        @Schema(description = "강의 ID") Long courseId,
        @Schema(description = "강의명") String title,
        @Schema(description = "신청자 회원 ID") Long applicantId,
        @Schema(description = "현재 신청자 수") int enrollCount,
        @Schema(description = "최대 수강 인원") Integer capacity) {

    public static EnrollmentApplyResponse of(Enrollment enrollment) {
        Course course = enrollment.getCourse();
        return new EnrollmentApplyResponse(
                enrollment.getId(),
                course.getId(),
                course.getTitle(),
                enrollment.getApplicant().getId(),
                course.getEnrollCount(),
                course.getCapacity());
    }
}
