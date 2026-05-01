package cwchoiit.weolbuexam.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import cwchoiit.weolbuexam.application.required.CourseRepository;
import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CourseOpenUseCaseTest {

    @Autowired CourseOpenUseCase courseOpenUseCase;
    @Autowired MemberRepository memberRepository;
    @Autowired CourseRepository courseRepository;
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
    void open() {
        CourseOpenPayload coursePayload = new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L);

        Course course = courseOpenUseCase.open(coursePayload, member.getId());

        assertThat(course.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        assertThat(courseRepository.findById(course.getId()).isPresent()).isTrue();
    }

    @Test
    void openFail() {
        CourseOpenPayload coursePayload = new CourseOpenPayload("a", 10, 200000L);

        assertThatThrownBy(() -> courseOpenUseCase.open(coursePayload, member.getId()))
                .isInstanceOf(ConstraintViolationException.class);

        CourseOpenPayload coursePayload2 = new CourseOpenPayload("a".repeat(201), 10, 200000L);

        assertThatThrownBy(() -> courseOpenUseCase.open(coursePayload2, member.getId()))
                .isInstanceOf(ConstraintViolationException.class);

        CourseOpenPayload coursePayload3 = new CourseOpenPayload("너나위의 내집마련 기초반", null, 200000L);

        assertThatThrownBy(() -> courseOpenUseCase.open(coursePayload3, member.getId()))
                .isInstanceOf(ConstraintViolationException.class);

        CourseOpenPayload coursePayload4 = new CourseOpenPayload("너나위의 내집마련 기초반", 10, null);

        assertThatThrownBy(() -> courseOpenUseCase.open(coursePayload4, member.getId()))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
