package cwchoiit.weolbuexam.application.required;

import static org.assertj.core.api.Assertions.assertThat;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.enrollment.Enrollment;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class EnrollmentRepositoryTest {

    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CourseRepository courseRepository;
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
    void existsByApplicantAndCourseWhenExists() {
        Enrollment enrollment = Enrollment.apply(course, student);
        enrollmentRepository.save(enrollment);

        entityManager.flush();
        entityManager.clear();

        assertThat(
                        enrollmentRepository.existsByApplicantAndCourse(
                                student, courseRepository.findById(course.getId()).orElseThrow()))
                .isTrue();
    }

    @Test
    void existsByApplicantAndCourseWhenNotExists() {
        entityManager.flush();
        entityManager.clear();

        assertThat(
                        enrollmentRepository.existsByApplicantAndCourse(
                                student, courseRepository.findById(course.getId()).orElseThrow()))
                .isFalse();
    }
}
