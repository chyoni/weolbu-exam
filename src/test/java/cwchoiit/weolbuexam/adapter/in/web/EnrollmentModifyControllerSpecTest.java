package cwchoiit.weolbuexam.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import cwchoiit.weolbuexam.adapter.in.web.request.EnrollmentApplyRequest;
import cwchoiit.weolbuexam.application.required.CourseRepository;
import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import java.util.List;
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
class EnrollmentModifyControllerSpecTest {

    @Autowired WebApplicationContext wac;
    @Autowired MemberRepository memberRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired EntityManager entityManager;

    ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;

    Member instructor;
    Member student;
    Course course;

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

        course = Course.open(new CourseOpenPayload("너나위의 내집마련 기초반", 10, 200000L), instructor);
        courseRepository.save(course);
    }

    @Test
    void apply() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/enrollments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new EnrollmentApplyRequest(
                                                        student.getId(), List.of(course.getId())))))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("너나위의 내집마련 기초반"))
                .andExpect(jsonPath("$.data[0].enrollCount").value(1))
                .andExpect(jsonPath("$.data[0].capacity").value(10));
    }

    @Test
    void applyFailWhenCapacityExceeded() throws Exception {
        Course tightCourse = Course.open(new CourseOpenPayload("한 명만 강의", 1, 50000L), instructor);
        courseRepository.save(tightCourse);

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/enrollments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new EnrollmentApplyRequest(
                                                        student.getId(),
                                                        List.of(tightCourse.getId())))))
                .andExpect(status().isAccepted());

        entityManager.flush();

        mockMvc.perform(
                        post("/api/v1/enrollments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new EnrollmentApplyRequest(
                                                        instructor.getId(),
                                                        List.of(tightCourse.getId())))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void applyFailWhenAlreadyEnrolled() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/enrollments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new EnrollmentApplyRequest(
                                                        student.getId(), List.of(course.getId())))))
                .andExpect(status().isAccepted());

        entityManager.flush();

        mockMvc.perform(
                        post("/api/v1/enrollments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new EnrollmentApplyRequest(
                                                        student.getId(), List.of(course.getId())))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
