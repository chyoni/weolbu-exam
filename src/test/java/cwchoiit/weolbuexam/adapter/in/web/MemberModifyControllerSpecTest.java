package cwchoiit.weolbuexam.adapter.in.web;

import static cwchoiit.weolbuexam.domain.member.MemberRole.STUDENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import cwchoiit.weolbuexam.adapter.in.web.request.MemberRegisterRequest;
import cwchoiit.weolbuexam.adapter.in.web.request.MemberUpdatePasswordRequest;
import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.member.Member;
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
class MemberModifyControllerSpecTest {

    @Autowired WebApplicationContext wac;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired MemberRepository memberRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired EntityManager entityManager;

    MockMvc mockMvc;

    Member member;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).build();
        member =
                Member.register(
                        new MemberRegisterPayload(
                                "최치원", "noreply@example.com", "01011112222", "secret123", STUDENT),
                        passwordEncoder);
        memberRepository.save(member);
    }

    @Test
    void register() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MemberRegisterRequest(
                                                        "홍길동",
                                                        "other@example.com",
                                                        "01099998888",
                                                        "Secret123",
                                                        STUDENT))))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.email").value("other@example.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("01099998888"))
                .andExpect(jsonPath("$.data.role").value("STUDENT"));
    }

    @Test
    void registerValidationFail() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MemberRegisterRequest(
                                                        "최치",
                                                        "other@example.com",
                                                        "01099998888",
                                                        "Secret123",
                                                        STUDENT))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MemberRegisterRequest(
                                                        "홍길동",
                                                        "invalid-email",
                                                        "01099998888",
                                                        "Secret123",
                                                        STUDENT))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MemberRegisterRequest(
                                                        "홍길동",
                                                        "other@example.com",
                                                        "01099998888",
                                                        "Sec1",
                                                        STUDENT))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void registerFailDuplicateEmail() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MemberRegisterRequest(
                                                        "홍길동",
                                                        "noreply@example.com",
                                                        "01099998888",
                                                        "Secret123",
                                                        STUDENT))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void registerFailDuplicatePhoneNumber() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MemberRegisterRequest(
                                                        "홍길동",
                                                        "other@example.com",
                                                        "01011112222",
                                                        "Secret123",
                                                        STUDENT))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void changePhoneNumber() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        put("/api/v1/members/" + member.getId() + "/phone")
                                .param("phoneNumber", "01022223333"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void changePhoneNumberFail() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        put("/api/v1/members/" + member.getId() + "/phone")
                                .param("phoneNumber", "01011112222"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void changePassword() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        put("/api/v1/members/" + member.getId() + "/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MemberUpdatePasswordRequest(
                                                        "secret123", "newSecret"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void changePasswordFail() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        put("/api/v1/members/" + member.getId() + "/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MemberUpdatePasswordRequest(
                                                        "wrong", "newSecret"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void changeRole() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        put("/api/v1/members/" + member.getId() + "/role")
                                .param("role", "INSTRUCTOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void changeRoleFail() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(put("/api/v1/members/" + member.getId() + "/role").param("role", "STUDENT"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
