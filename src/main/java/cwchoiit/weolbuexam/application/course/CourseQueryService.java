package cwchoiit.weolbuexam.application.course;

import cwchoiit.weolbuexam.application.provided.CourseLoadUseCase;
import cwchoiit.weolbuexam.application.required.CourseRepository;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseSearchPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseQueryService implements CourseLoadUseCase {

    private final CourseRepository courseRepository;

    @Override
    public Page<Course> loadCourses(@Valid CourseSearchPayload payload) {
        return courseRepository.findCourses(payload);
    }
}
