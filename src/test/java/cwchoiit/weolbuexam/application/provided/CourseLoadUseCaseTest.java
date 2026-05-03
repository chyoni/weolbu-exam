package cwchoiit.weolbuexam.application.provided;

import static org.assertj.core.api.Assertions.assertThat;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import cwchoiit.weolbuexam.application.required.CourseRepository;
import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.course.payload.CourseSearchPayload;
import cwchoiit.weolbuexam.domain.course.payload.CourseSortType;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CourseLoadUseCaseTest {

    @Autowired CourseLoadUseCase courseLoadUseCase;
    @Autowired MemberRepository memberRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired EntityManager entityManager;

    PasswordEncoder passwordEncoder = new SecurePasswordEncoder();
    Member instructor;

    @BeforeEach
    void setUp() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret",
                        MemberRole.INSTRUCTOR);

        instructor = Member.register(registerPayload, passwordEncoder);

        memberRepository.save(instructor);
    }

    @Test
    void loadCourses() {
        Course course =
                Course.open(new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L), instructor);
        courseRepository.save(course);

        entityManager.flush();
        entityManager.clear();

        Page<Course> page =
                courseLoadUseCase.loadCourses(
                        new CourseSearchPayload(CourseSortType.CREATED_AT_DESC, 0, 20));

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getId()).isEqualTo(course.getId());
    }
}
