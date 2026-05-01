package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import jakarta.validation.Valid;

/** 강좌를 등록한다 */
public interface CourseOpenUseCase {
    Course open(@Valid CourseOpenPayload openPayload, Long instructorId);
}
