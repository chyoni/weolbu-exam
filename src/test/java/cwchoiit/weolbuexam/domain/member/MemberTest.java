package cwchoiit.weolbuexam.domain.member;

import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MemberTest {

    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @Test
    void register() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "Secret", MemberRole.STUDENT);

        Member member = Member.register(registerPayload, passwordEncoder);

        assertThat(member.getName()).isEqualTo(registerPayload.name());
        assertThat(member.getEmail().address()).isEqualTo(registerPayload.email());
        assertThat(member.getPhoneNumber().value()).isEqualTo(registerPayload.phoneNumber());
        assertThat(member.getRole()).isEqualTo(registerPayload.role());

        verify(passwordEncoder, times(1)).encode(eq(registerPayload.rawPassword()));
    }

    @Test
    void registerFailName() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        null, "noreply@example.com", "01011112222", "Secret", MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload, passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void registerFailEmail() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload("최치원", null, "01011112222", "Secret", MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload, passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void registerFailPhoneNumber() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", null, "Secret", MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload, passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void registerFailPassword() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", null, MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload, passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void registerFailRole() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "Secret", null);

        assertThatThrownBy(() -> Member.register(registerPayload, passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void isInstructor() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "Secret", MemberRole.STUDENT);

        Member member = Member.register(registerPayload, passwordEncoder);

        assertThat(member.isInstructor()).isFalse();

        member.changeRole(MemberRole.INSTRUCTOR);

        assertThat(member.isInstructor()).isTrue();
    }

    @Test
    void changePassword() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "Secret", MemberRole.STUDENT);

        when(passwordEncoder.encode(eq("Secret"))).thenReturn("prevEncodedPassword");

        Member member = Member.register(registerPayload, passwordEncoder);

        assertThat(member.getName()).isEqualTo(registerPayload.name());

        when(passwordEncoder.encode(eq("new12345"))).thenReturn("newEncodedPassword");

        when(passwordEncoder.matches(eq("Secret"), any())).thenReturn(true);

        member.changePassword("Secret", "new12345", passwordEncoder);

        verify(passwordEncoder, times(1)).encode(eq("new12345"));

        assertThat(member.getEncodedPassword()).isEqualTo("newEncodedPassword");
    }

    @Test
    void verifyPassword() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "Secret", MemberRole.STUDENT);

        Member member = Member.register(registerPayload, passwordEncoder);

        when(passwordEncoder.encode(eq("Secret"))).thenReturn("encodedSecret");
        when(passwordEncoder.matches(eq("Secret"), any())).thenReturn(true);

        assertThat(member.verifyPassword("Secret", passwordEncoder)).isTrue();
    }

    @Test
    void changePasswordFailLength() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "Sec", MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload, passwordEncoder))
                .isInstanceOf(IllegalStateException.class);

        MemberRegisterPayload registerPayload2 =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret123000444",
                        MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload2, passwordEncoder))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void changePasswordFailCombination() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "SECRET", MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload, passwordEncoder))
                .isInstanceOf(IllegalStateException.class);

        MemberRegisterPayload registerPayload2 =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "12345678",
                        MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload2, passwordEncoder))
                .isInstanceOf(IllegalStateException.class);

        MemberRegisterPayload registerPayload3 =
                new MemberRegisterPayload(
                        "최치원", "noreply@example.com", "01011112222", "secret", MemberRole.STUDENT);

        assertThatThrownBy(() -> Member.register(registerPayload3, passwordEncoder))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void changeRole() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret123",
                        MemberRole.STUDENT);

        Member member = Member.register(registerPayload, passwordEncoder);

        assertThat(member.getRole()).isEqualTo(MemberRole.STUDENT);

        member.changeRole(MemberRole.INSTRUCTOR);

        assertThat(member.getRole()).isEqualTo(MemberRole.INSTRUCTOR);
    }

    @Test
    void changeRoleFail() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret123",
                        MemberRole.STUDENT);

        Member member = Member.register(registerPayload, passwordEncoder);

        assertThatThrownBy(() -> member.changeRole(MemberRole.STUDENT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changePhoneNumber() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret123",
                        MemberRole.STUDENT);

        Member member = Member.register(registerPayload, passwordEncoder);

        String newPhoneNumber = "01022223333";
        member.changePhoneNumber(newPhoneNumber);

        assertThat(member.getPhoneNumber().value()).isEqualTo(newPhoneNumber);
    }

    @Test
    void changePhoneNumberFail() {
        MemberRegisterPayload registerPayload =
                new MemberRegisterPayload(
                        "최치원",
                        "noreply@example.com",
                        "01011112222",
                        "Secret123",
                        MemberRole.STUDENT);

        Member member = Member.register(registerPayload, passwordEncoder);

        String newPhoneNumber = "1234";
        assertThatThrownBy(() -> member.changePhoneNumber(newPhoneNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
