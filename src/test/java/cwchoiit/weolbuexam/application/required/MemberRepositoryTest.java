package cwchoiit.weolbuexam.application.required;

import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static cwchoiit.weolbuexam.domain.member.MemberRole.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired EntityManager entityManager;

    Member member;

    @BeforeEach
    void setUp() {
        member =
                Member.register(
                        new MemberRegisterPayload(
                                "최치원", "noreply@example.com", "01011112222", "secret123", STUDENT),
                        passwordEncoder);
    }

    @Test
    void save() {
        assertThat(member.getId()).isNull();

        Member createMember = memberRepository.save(member);

        entityManager.flush();
        entityManager.clear();

        assertThat(createMember.getId()).isNotNull();
    }

    @Test
    void findByEmail() {
        Member createMember = memberRepository.save(member);

        entityManager.flush();
        entityManager.clear();

        assertThat(memberRepository.findByEmail(createMember.getEmail())).isNotNull();
    }

    @Test
    void findByPhoneNumber() {
        Member createMember = memberRepository.save(member);

        entityManager.flush();
        entityManager.clear();

        assertThat(memberRepository.findByPhoneNumber(createMember.getPhoneNumber())).isNotNull();
    }
}
