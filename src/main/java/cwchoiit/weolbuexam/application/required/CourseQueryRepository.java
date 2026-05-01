package cwchoiit.weolbuexam.application.required;

import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseSearchPayload;
import org.springframework.data.domain.Page;

public interface CourseQueryRepository {
    Page<Course> findCourses(CourseSearchPayload payload);
}
