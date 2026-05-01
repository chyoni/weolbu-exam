package cwchoiit.weolbuexam.domain.course;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CourseTest {

    PasswordEncoder passwordEncoder = new SecurePasswordEncoder();

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

        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000);

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

        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000);

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

        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000);

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

        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 1, 200000);

        Course course = Course.open(coursePayload, member);

        assertThat(course.getEnrollCount()).isEqualTo(0);

        course.increaseEnrollCount();

        assertThat(course.getEnrollCount()).isEqualTo(1);

        assertThatThrownBy(course::increaseEnrollCount).isInstanceOf(IllegalStateException.class);
    }
}
