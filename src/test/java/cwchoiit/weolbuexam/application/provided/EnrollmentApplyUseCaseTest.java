package cwchoiit.weolbuexam.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import cwchoiit.weolbuexam.application.required.CourseRepository;
import cwchoiit.weolbuexam.application.required.EnrollmentRepository;
import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.enrollment.Enrollment;
import cwchoiit.weolbuexam.domain.enrollment.payload.EnrollmentApplyPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class EnrollmentApplyUseCaseTest {

    @Autowired EnrollmentApplyUseCase enrollmentApplyUseCase;
    @Autowired MemberRepository memberRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired EntityManager entityManager;

    PasswordEncoder passwordEncoder = new SecurePasswordEncoder();

    Member instructor;
    Member student;
    Course course;

    @BeforeEach
    void setUp() {
        instructor =
                Member.register(
                        new MemberRegisterPayload(
                                "강사최치원",
                                "instructor@example.com",
                                "01011112222",
                                "Secret1",
                                MemberRole.INSTRUCTOR),
                        passwordEncoder);
        memberRepository.save(instructor);

        student =
                Member.register(
                        new MemberRegisterPayload(
                                "수강생홍길동",
                                "student@example.com",
                                "01022223333",
                                "Secret2",
                                MemberRole.STUDENT),
                        passwordEncoder);
        memberRepository.save(student);

        course = Course.open(new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L), instructor);
        courseRepository.save(course);
    }

    @Test
    void apply() {
        List<Enrollment> enrollments =
                enrollmentApplyUseCase.apply(
                        new EnrollmentApplyPayload(List.of(course.getId())), student.getId());

        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.getFirst().getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        assertThat(
                        enrollmentRepository.existsByApplicantAndCourse(
                                student, courseRepository.findById(course.getId()).orElseThrow()))
                .isTrue();
    }

    @Test
    void applyMultipleCourses() {
        Course course2 = Course.open(new CourseOpenPayload("제2강의", 5, 100000L), instructor);
        courseRepository.save(course2);

        List<Enrollment> enrollments =
                enrollmentApplyUseCase.apply(
                        new EnrollmentApplyPayload(List.of(course.getId(), course2.getId())),
                        student.getId());

        assertThat(enrollments).hasSize(2);
    }

    @Test
    void applyByInstructor() {
        List<Enrollment> enrollments =
                enrollmentApplyUseCase.apply(
                        new EnrollmentApplyPayload(List.of(course.getId())), instructor.getId());

        assertThat(enrollments).hasSize(1);
    }

    @Test
    void applyFailWhenCapacityExceeded() {
        Course fullCourse = Course.open(new CourseOpenPayload("정원 1명 강의", 1, 50000L), instructor);
        courseRepository.save(fullCourse);

        enrollmentApplyUseCase.apply(
                new EnrollmentApplyPayload(List.of(fullCourse.getId())), student.getId());

        assertThatThrownBy(
                        () ->
                                enrollmentApplyUseCase.apply(
                                        new EnrollmentApplyPayload(List.of(fullCourse.getId())),
                                        instructor.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("최대 수강 인원을 초과했습니다");
    }

    @Test
    void applyFailWhenAlreadyEnrolled() {
        enrollmentApplyUseCase.apply(
                new EnrollmentApplyPayload(List.of(course.getId())), student.getId());

        entityManager.flush();

        assertThatThrownBy(
                        () ->
                                enrollmentApplyUseCase.apply(
                                        new EnrollmentApplyPayload(List.of(course.getId())),
                                        student.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 신청한 강의입니다");
    }

    @Test
    void applyFailWhenCourseNotFound() {
        assertThatThrownBy(
                        () ->
                                enrollmentApplyUseCase.apply(
                                        new EnrollmentApplyPayload(List.of(99999L)),
                                        student.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("강의를 찾을 수 없습니다");
    }

    @Test
    void applyValidationFail() {
        assertThatThrownBy(
                        () ->
                                enrollmentApplyUseCase.apply(
                                        new EnrollmentApplyPayload(List.of()), student.getId()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void applyDuplicateCourseIds() {
        List<Enrollment> enrollments =
                enrollmentApplyUseCase.apply(
                        new EnrollmentApplyPayload(List.of(course.getId(), course.getId())),
                        student.getId());

        assertThat(enrollments).hasSize(1);
    }
}
