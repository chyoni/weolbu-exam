package cwchoiit.weolbuexam.domain.member;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.state;

import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import cwchoiit.weolbuexam.domain.member.vo.Email;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {
    private String name;
    private Email email;
    private String phoneNumber;
    private String encodedPassword;
    private MemberRole role;

    public static Member register(
            MemberRegisterPayload registerPayload, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.name = requireNonNull(registerPayload.name());
        member.email = new Email(requireNonNull(registerPayload.email()));
        member.phoneNumber = requireNonNull(registerPayload.phoneNumber());

        checkPassword(requireNonNull(registerPayload.rawPassword()));

        member.encodedPassword = passwordEncoder.encode(registerPayload.rawPassword());
        member.role = requireNonNull(registerPayload.role());

        return member;
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        checkPassword(newPassword);

        this.encodedPassword = passwordEncoder.encode(newPassword);
    }

    public void changeRole(MemberRole newRole) {
        isTrue(this.role != newRole, "이미 변경하고자 하는 회원 유형입니다: " + newRole);

        this.role = newRole;
    }

    public void changePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    private static void checkPassword(String rawPassword) {
        state(
                rawPassword.length() >= 6 && rawPassword.length() <= 10,
                "패스워드는 최소 6자 이상 10자 이하이어야 합니다");
        state(
                rawPassword.matches(
                        "^(?:(?=.*[a-z])(?=.*[A-Z])|(?=.*[a-z])(?=.*\\d)|(?=.*[A-Z])(?=.*\\d)).*$"),
                "패스워드는 영문 소문자, 대문자, 숫자 중 최소 두 가지 이상 조합이 필요합니다");
    }
}
