package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static cwchoiit.weolbuexam.domain.member.MemberRole.INSTRUCTOR;
import static cwchoiit.weolbuexam.domain.member.MemberRole.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class MemberUpdateUseCaseTest {

    @Autowired MemberUpdateUseCase memberUpdateUseCase;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired MemberRepository memberRepository;

    Member member;

    @BeforeEach
    void setUp() {
        member =
                Member.register(
                        new MemberRegisterPayload(
                                "최치원", "noreply@example.com", "01011112222", "secret123", STUDENT),
                        passwordEncoder);
        memberRepository.save(member);
    }

    @Test
    void changePhoneNumber() {
        memberUpdateUseCase.changePhoneNumber(member.getId(), "01022223333");

        assertThat(member.getPhoneNumber().value()).isEqualTo("01022223333");
    }

    @Test
    void changePhoneNumberFail() {
        assertThatThrownBy(
                        () -> memberUpdateUseCase.changePhoneNumber(member.getId(), "01011112222"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeRole() {
        memberUpdateUseCase.changeRole(member.getId(), INSTRUCTOR);

        assertThat(member.getRole()).isEqualTo(INSTRUCTOR);
    }

    @Test
    void changeRoleFail() {
        assertThatThrownBy(() -> memberUpdateUseCase.changeRole(member.getId(), STUDENT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changePassword() {
        memberUpdateUseCase.changePassword(member.getId(), "secret123", "newSecret");

        assertThat(member.verifyPassword("newSecret", passwordEncoder)).isTrue();
    }

    @Test
    void changePasswordFail() {
        assertThatThrownBy(
                        () ->
                                memberUpdateUseCase.changePassword(
                                        member.getId(), "invalid", "newSecret"))
                .isInstanceOf(IllegalStateException.class);
    }
}
