package cwchoiit.weolbuexam.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import cwchoiit.weolbuexam.adapter.in.web.request.CourseOpenRequest;
import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@SpringBootTest
class CourseModifyControllerSpecTest {

    @Autowired WebApplicationContext wac;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired EntityManager entityManager;

    ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;

    Member instructor;
    Member student;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).build();

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
    }

    @Test
    void open() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new CourseOpenRequest(
                                                        instructor.getId(),
                                                        "너나위의 내집마련 기초반",
                                                        30,
                                                        200000L))))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("너나위의 내집마련 기초반"))
                .andExpect(jsonPath("$.data.capacity").value(30))
                .andExpect(jsonPath("$.data.price").value(200000))
                .andExpect(jsonPath("$.data.instructorName").value("강사최치원"));
    }

    @Test
    void openFailWhenStudent() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new CourseOpenRequest(
                                                        student.getId(),
                                                        "너나위의 내집마련 기초반",
                                                        30,
                                                        200000L))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void openValidationFail() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new CourseOpenRequest(
                                                        instructor.getId(), "짧", 30, 200000L))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
