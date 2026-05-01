package cwchoiit.weolbuexam.adapter.in.web;

import static cwchoiit.weolbuexam.domain.member.MemberRole.STUDENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import cwchoiit.weolbuexam.application.required.MemberRepository;
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
class MemberQueryControllerSpecTest {

    @Autowired WebApplicationContext wac;

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
    void findMember() throws Exception {
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(get("/api/v1/members/" + member.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.memberId").value(member.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value("최치원"))
                .andExpect(jsonPath("$.data.email").value("noreply@example.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("01011112222"))
                .andExpect(jsonPath("$.data.role").value("STUDENT"));
    }

    @Test
    void findMemberFail() throws Exception {
        mockMvc.perform(get("/api/v1/members/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
