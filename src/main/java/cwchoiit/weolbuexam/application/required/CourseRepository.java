package cwchoiit.weolbuexam.application.required;

import cwchoiit.weolbuexam.domain.course.Course;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface CourseRepository extends Repository<Course, Long> {
    Course save(Course course);

    Optional<Course> findById(Long courseId);
}
