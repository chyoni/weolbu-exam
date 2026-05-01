package cwchoiit.weolbuexam.application.provided;

import static cwchoiit.weolbuexam.domain.member.MemberRole.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberRegisterUseCaseTest {

    @Autowired MemberRegisterUseCase memberRegisterUseCase;

    @Test
    void register() {
        Member member =
                memberRegisterUseCase.register(
                        new MemberRegisterPayload(
                                "최치원", "noreply@example.com", "01011112222", "secret123", STUDENT));

        assertThat(member.getId()).isNotNull();
    }

    @Test
    void registerValidationFail() {
        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.register(
                                        new MemberRegisterPayload(
                                                "최치",
                                                "noreply@example.com",
                                                "01011112222",
                                                "secret123",
                                                STUDENT)))
                .isInstanceOf(ConstraintViolationException.class);

        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.register(
                                        new MemberRegisterPayload(
                                                "최치원ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo",
                                                "noreply@example.com",
                                                "01011112222",
                                                "secret123",
                                                STUDENT)))
                .isInstanceOf(ConstraintViolationException.class);

        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.register(
                                        new MemberRegisterPayload(
                                                "최치원",
                                                "invalid",
                                                "01011112222",
                                                "secret123",
                                                STUDENT)))
                .isInstanceOf(ConstraintViolationException.class);

        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.register(
                                        new MemberRegisterPayload(
                                                "최치원",
                                                "noreply@example.com",
                                                "12",
                                                "secret123",
                                                STUDENT)))
                .isInstanceOf(ConstraintViolationException.class);

        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.register(
                                        new MemberRegisterPayload(
                                                "최치원",
                                                "noreply@example.com",
                                                "01011112222",
                                                "1",
                                                STUDENT)))
                .isInstanceOf(ConstraintViolationException.class);

        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.register(
                                        new MemberRegisterPayload(
                                                "최치원",
                                                "noreply@example.com",
                                                "01011112222",
                                                "secret123123123123",
                                                STUDENT)))
                .isInstanceOf(ConstraintViolationException.class);

        assertThatThrownBy(
                        () ->
                                memberRegisterUseCase.register(
                                        new MemberRegisterPayload(
                                                "최치원",
                                                "noreply@example.com",
                                                "01011112222",
                                                "secret123",
                                                null)))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void registerFailDuplicateEmail() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "secret123", STUDENT);
        Member member = memberRegisterUseCase.register(registerPayload);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail().address()).isEqualTo(registerPayload.email());

        assertThatThrownBy(() -> memberRegisterUseCase.register(registerPayload))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void registerFailDuplicatePhoneNumber() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "secret123", STUDENT);
        Member member = memberRegisterUseCase.register(registerPayload);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail().address()).isEqualTo(registerPayload.email());

        MemberRegisterPayload registerPayload2 =
                new MemberRegisterPayload(
                        "최치원", "noreply2@example.com", "01011112222", "secret123", STUDENT);

        assertThatThrownBy(() -> memberRegisterUseCase.register(registerPayload2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
