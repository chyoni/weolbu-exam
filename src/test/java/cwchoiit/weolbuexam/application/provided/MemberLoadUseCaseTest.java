package cwchoiit.weolbuexam.application.provided;

import static cwchoiit.weolbuexam.domain.member.MemberRole.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;

import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberLoadUseCaseTest {

    @Autowired MemberLoadUseCase memberLoadUseCase;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired MemberRepository memberRepository;

    @Autowired EntityManager entityManager;

    @Test
    void find() {
        Member member =
                Member.register(
                        new MemberRegisterPayload(
                                "최치원", "noreply@example.com", "01011112222", "secret123", STUDENT),
                        passwordEncoder);
        memberRepository.save(member);

        entityManager.flush();
        entityManager.clear();

        Member findMember = memberLoadUseCase.find(member.getId());

        assertThat(findMember).isNotNull();
        assertThat(findMember.getId()).isEqualTo(member.getId());
    }
}
