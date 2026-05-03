package cwchoiit.weolbuexam.domain.enrollment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnrollmentTest {

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
        setField(instructor, "id", 1L);

        student =
                Member.register(
                        new MemberRegisterPayload(
                                "수강생홍길동",
                                "student@example.com",
                                "01022223333",
                                "Secret2",
                                MemberRole.STUDENT),
                        passwordEncoder);
        setField(student, "id", 2L);

        course = Course.open(new CourseOpenPayload("너나위의 내집마련 기초반", 1, 200000L), instructor);
        setField(course, "id", 1L);
    }

    @Test
    void apply() {
        Enrollment enrollment = Enrollment.apply(course, student);

        assertThat(enrollment.getCourse()).isEqualTo(course);
        assertThat(enrollment.getApplicant()).isEqualTo(student);
        assertThat(enrollment.getEnrolledAt()).isNotNull();
        assertThat(course.getEnrollCount()).isEqualTo(1);
    }

    @Test
    void applyFailWhenCapacityExceeded() {
        Enrollment.apply(course, student);

        assertThatThrownBy(() -> Enrollment.apply(course, instructor))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("최대 수강 인원을 초과했습니다");
    }

    @Test
    void equalsSameInstance() {
        Enrollment enrollment = Enrollment.apply(course, student);
        assertThat(enrollment).isEqualTo(enrollment);
    }

    @Test
    void equalsById() {
        Course course2 = Course.open(new CourseOpenPayload("다른 강의", 1, 100000L), instructor);
        setField(course2, "id", 2L);

        Enrollment a = Enrollment.apply(course, student);
        setField(a, "id", 1L);

        Enrollment b = Enrollment.apply(course2, student);
        setField(b, "id", 2L);

        assertThat(a).isNotEqualTo(b);
        assertThat(a).isNotEqualTo(null);
    }
}
