package cwchoiit.weolbuexam.application.required;

import static org.assertj.core.api.Assertions.assertThat;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
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
class CourseQueryRepositoryTest {

    @Autowired CourseRepository courseRepository;
    @Autowired MemberRepository memberRepository;
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
    void findCoursesByCreatedAtDesc() {
        Course oldest = saveCourse("강의1", 10, 0);
        Course middle = saveCourse("강의2", 10, 0);
        Course newest = saveCourse("강의3", 10, 0);

        entityManager.flush();
        entityManager.clear();

        Page<Course> page =
                courseRepository.findCourses(
                        new CourseSearchPayload(CourseSortType.CREATED_AT_DESC, 0, 20));

        assertThat(page.getContent())
                .extracting(Course::getId)
                .containsExactly(newest.getId(), middle.getId(), oldest.getId());
    }

    @Test
    void findCoursesByEnrollCountDesc() {
        Course low = saveCourse("강의1", 100, 5);
        Course high = saveCourse("강의2", 100, 20);
        Course mid = saveCourse("강의3", 100, 10);

        entityManager.flush();
        entityManager.clear();

        Page<Course> page =
                courseRepository.findCourses(
                        new CourseSearchPayload(CourseSortType.ENROLL_COUNT_DESC, 0, 20));

        assertThat(page.getContent())
                .extracting(Course::getId)
                .containsExactly(high.getId(), mid.getId(), low.getId());
    }

    @Test
    void findCoursesByEnrollRateDesc() {
        Course rate20 = saveCourse("강의1", 100, 20);
        Course rate50 = saveCourse("강의2", 10, 5);
        Course rate07 = saveCourse("강의3", 100, 7);

        entityManager.flush();
        entityManager.clear();

        Page<Course> page =
                courseRepository.findCourses(
                        new CourseSearchPayload(CourseSortType.ENROLL_RATE_DESC, 0, 20));

        assertThat(page.getContent())
                .extracting(Course::getId)
                .containsExactly(rate50.getId(), rate20.getId(), rate07.getId());
    }

    @Test
    void findCoursesPagination() {
        for (int i = 0; i < 25; i++) {
            saveCourse("강의" + i, 10, 0);
        }

        entityManager.flush();
        entityManager.clear();

        Page<Course> firstPage =
                courseRepository.findCourses(
                        new CourseSearchPayload(CourseSortType.CREATED_AT_DESC, 0, 20));

        assertThat(firstPage.getContent()).hasSize(20);
        assertThat(firstPage.getTotalElements()).isEqualTo(25);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);

        Page<Course> secondPage =
                courseRepository.findCourses(
                        new CourseSearchPayload(CourseSortType.CREATED_AT_DESC, 1, 20));

        assertThat(secondPage.getContent()).hasSize(5);
    }

    private Course saveCourse(String title, int capacity, int enrollCount) {
        Course course = Course.open(new CourseOpenPayload(title, capacity, 100000L), instructor);
        for (int i = 0; i < enrollCount; i++) {
            course.increaseEnrollCount();
        }
        return courseRepository.save(course);
    }
}
