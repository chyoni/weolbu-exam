package cwchoiit.weolbuexam.adapter.in.web.response;

import cwchoiit.weolbuexam.domain.course.Course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "강의 등록 결과")
public record CourseOpenResponse(
        @Schema(description = "강의 ID") Long courseId,
        @Schema(description = "강의명") String title,
        @Schema(description = "최대 수강 인원") Integer capacity,
        @Schema(description = "가격") Long price,
        @Schema(description = "강사명") String instructorName) {

    public static CourseOpenResponse of(Course course) {
        return new CourseOpenResponse(
                course.getId(),
                course.getTitle(),
                course.getCapacity(),
                course.getPrice(),
                course.getInstructor().getName());
    }
}
