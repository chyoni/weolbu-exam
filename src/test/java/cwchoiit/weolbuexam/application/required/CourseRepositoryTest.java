package cwchoiit.weolbuexam.application.required;

import static org.assertj.core.api.Assertions.assertThat;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
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
class CourseRepositoryTest {

    @Autowired CourseRepository courseRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager entityManager;

    PasswordEncoder passwordEncoder = new SecurePasswordEncoder();
    Member member;

    @BeforeEach
    void setUp() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret",
                        MemberRole.INSTRUCTOR);

        member = Member.register(registerPayload, passwordEncoder);

        memberRepository.save(member);
    }

    @Test
    void save() {
        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L);

        Course course = Course.open(coursePayload, member);

        assertThat(course.getId()).isNull();

        Course newCourse = courseRepository.save(course);

        assertThat(newCourse.getId()).isNotNull();
    }

    @Test
    void findById() {
        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L);

        Course course = Course.open(coursePayload, member);

        assertThat(course.getId()).isNull();

        Course newCourse = courseRepository.save(course);

        entityManager.flush();
        entityManager.clear();

        assertThat(newCourse.getId()).isNotNull();

        Course findCourse = courseRepository.findById(newCourse.getId()).orElseThrow();

        assertThat(findCourse.getId()).isEqualTo(newCourse.getId());
    }
}
