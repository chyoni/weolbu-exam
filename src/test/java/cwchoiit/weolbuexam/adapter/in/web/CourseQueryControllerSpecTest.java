package cwchoiit.weolbuexam.adapter.in.web;

import static cwchoiit.weolbuexam.domain.member.MemberRole.INSTRUCTOR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import cwchoiit.weolbuexam.application.required.CourseRepository;
import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@SpringBootTest
class CourseQueryControllerSpecTest {

    @Autowired WebApplicationContext wac;

    @Autowired MemberRepository memberRepository;

    @Autowired CourseRepository courseRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired EntityManager entityManager;

    MockMvc mockMvc;

    Member instructor;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).build();
        instructor =
                Member.register(
                        new MemberRegisterPayload(
                                "최치원",
                                "noreply@example.com",
                                "01011112222",
                                "secret123",
                                INSTRUCTOR),
                        passwordEncoder);
        memberRepository.save(instructor);
    }

    @Test
    void findCourses() throws Exception {
        Course course =
                Course.open(new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L), instructor);
        courseRepository.save(course);

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(20))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("너나위의 내집마련 기초반"))
                .andExpect(jsonPath("$.data.content[0].instructorName").value("최치원"))
                .andExpect(jsonPath("$.data.content[0].capacity").value(10))
                .andExpect(jsonPath("$.data.content[0].enrollCount").value(0))
                .andExpect(jsonPath("$.data.content[0].price").value(200000));
    }

    @Test
    void findCoursesEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalElements").value(0));
    }
}
