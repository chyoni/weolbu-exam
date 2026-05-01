package cwchoiit.weolbuexam.adapter.in.web.response;

import cwchoiit.weolbuexam.domain.course.Course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "강의 목록 항목")
public record CourseListItemResponse(
        @Schema(description = "강의 ID") Long courseId,
        @Schema(description = "강의명") String title,
        @Schema(description = "가격") Long price,
        @Schema(description = "강사명") String instructorName,
        @Schema(description = "현재 신청자 수") int enrollCount,
        @Schema(description = "최대 수강 인원") Integer capacity) {

    public static CourseListItemResponse of(Course course) {
        return new CourseListItemResponse(
                course.getId(),
                course.getTitle(),
                course.getPrice(),
                course.getInstructor().getName(),
                course.getEnrollCount(),
                course.getCapacity());
    }
}
