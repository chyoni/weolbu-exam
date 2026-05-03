package cwchoiit.weolbuexam.application.enrollment;

import static org.springframework.util.Assert.state;

import cwchoiit.weolbuexam.application.provided.EnrollmentApplyUseCase;
import cwchoiit.weolbuexam.application.provided.MemberLoadUseCase;
import cwchoiit.weolbuexam.application.required.CourseRepository;
import cwchoiit.weolbuexam.application.required.EnrollmentRepository;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.enrollment.Enrollment;
import cwchoiit.weolbuexam.domain.enrollment.payload.EnrollmentApplyPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class EnrollmentModifyService implements EnrollmentApplyUseCase {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    private final MemberLoadUseCase memberLoadUseCase;

    @Override
    public List<Enrollment> apply(@Valid EnrollmentApplyPayload payload, Long applicantId) {
        Member applicant = memberLoadUseCase.find(applicantId);

        List<Long> sortedCourseIds = payload.courseIds().stream().distinct().sorted().toList();

        return sortedCourseIds.stream().map(courseId -> applyOne(courseId, applicant)).toList();
    }

    private Enrollment applyOne(Long courseId, Member applicant) {
        Course course =
                courseRepository
                        .findByIdForUpdate(courseId)
                        .orElseThrow(
                                () -> new NoSuchElementException("강의를 찾을 수 없습니다: " + courseId));

        state(
                !enrollmentRepository.existsByApplicantAndCourse(applicant, course),
                "이미 신청한 강의입니다: " + course.getTitle());

        return enrollmentRepository.save(Enrollment.apply(course, applicant));
    }
}
