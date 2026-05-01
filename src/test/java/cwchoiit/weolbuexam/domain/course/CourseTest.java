package cwchoiit.weolbuexam.domain.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class CourseTest {

    PasswordEncoder passwordEncoder = new SecurePasswordEncoder();

    Member instructor =
            Member.register(
                    new MemberRegisterPayload(
                            "최치원",
                            "noreply@example.com",
                            "01011112222",
                            "Secret",
                            MemberRole.INSTRUCTOR),
                    passwordEncoder);

    @Test
    void open() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret",
                        MemberRole.INSTRUCTOR);

        Member member = Member.register(registerPayload, passwordEncoder);

        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L);

        Course course = Course.open(coursePayload, member);

        assertThat(course.getTitle()).isEqualTo(coursePayload.title());
        assertThat(course.getCapacity()).isEqualTo(coursePayload.capacity());
        assertThat(course.getEnrollCount()).isEqualTo(0);
    }

    @Test
    void openFail() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "Secret", MemberRole.STUDENT);

        Member member = Member.register(registerPayload, passwordEncoder);

        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L);

        assertThatThrownBy(() -> Course.open(coursePayload, member))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void increaseEnrollCount() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret",
                        MemberRole.INSTRUCTOR);

        Member member = Member.register(registerPayload, passwordEncoder);

        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L);

        Course course = Course.open(coursePayload, member);

        assertThat(course.getEnrollCount()).isEqualTo(0);

        course.increaseEnrollCount();

        assertThat(course.getEnrollCount()).isEqualTo(1);
    }

    @Test
    void increaseEnrollCountFail() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret",
                        MemberRole.INSTRUCTOR);

        Member member = Member.register(registerPayload, passwordEncoder);

        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 1, 200000L);

        Course course = Course.open(coursePayload, member);

        assertThat(course.getEnrollCount()).isEqualTo(0);

        course.increaseEnrollCount();

        assertThat(course.getEnrollCount()).isEqualTo(1);

        assertThatThrownBy(course::increaseEnrollCount).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void equalsSameInstance() {
        Course course = Course.open(new CourseOpenPayload("내집마련 기초반", 10, 200000L), instructor);

        assertThat(course).isEqualTo(course);
    }

    @Test
    void equalsNull() {
        Course course = Course.open(new CourseOpenPayload("내집마련 기초반", 10, 200000L), instructor);

        assertThat(course).isNotEqualTo(null);
    }

    @Test
    void equalsDifferentType() {
        Course course = Course.open(new CourseOpenPayload("내집마련 기초반", 10, 200000L), instructor);

        assertThat(course.equals("String")).isFalse();
    }

    @Test
    void equalsBothUnpersisted() {
        Course a = Course.open(new CourseOpenPayload("내집마련 기초반", 10, 200000L), instructor);
        Course b = Course.open(new CourseOpenPayload("주식 투자 기초반", 20, 150000L), instructor);

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equalsSameId() {
        Course a = Course.open(new CourseOpenPayload("내집마련 기초반", 10, 200000L), instructor);
        Course b = Course.open(new CourseOpenPayload("주식 투자 기초반", 20, 150000L), instructor);

        ReflectionTestUtils.setField(a, "id", 1L);
        ReflectionTestUtils.setField(b, "id", 1L);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void equalsDifferentId() {
        Course a = Course.open(new CourseOpenPayload("내집마련 기초반", 10, 200000L), instructor);
        Course b = Course.open(new CourseOpenPayload("주식 투자 기초반", 20, 150000L), instructor);

        ReflectionTestUtils.setField(a, "id", 1L);
        ReflectionTestUtils.setField(b, "id", 2L);

        assertThat(a).isNotEqualTo(b);
    }
}
