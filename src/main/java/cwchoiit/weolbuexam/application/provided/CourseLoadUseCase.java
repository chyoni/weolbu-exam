package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseSearchPayload;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

/** 강좌 목록을 조회한다 */
public interface CourseLoadUseCase {
    Page<Course> loadCourses(@Valid CourseSearchPayload payload);
}
