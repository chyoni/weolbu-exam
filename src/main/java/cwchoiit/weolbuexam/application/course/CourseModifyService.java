package cwchoiit.weolbuexam.application.course;

import cwchoiit.weolbuexam.application.provided.CourseOpenUseCase;
import cwchoiit.weolbuexam.application.provided.MemberLoadUseCase;
import cwchoiit.weolbuexam.application.required.CourseRepository;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class CourseModifyService implements CourseOpenUseCase {

    private final CourseRepository courseRepository;

    private final MemberLoadUseCase memberLoadUseCase;

    @Override
    public Course open(@Valid CourseOpenPayload openPayload, Long instructorId) {
        Member instructor = memberLoadUseCase.find(instructorId);

        Course course = Course.open(openPayload, instructor);

        return courseRepository.save(course);
    }
}
