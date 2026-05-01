package cwchoiit.weolbuexam.domain.member;

import cwchoiit.weolbuexam.domain.BaseEntity;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import cwchoiit.weolbuexam.domain.member.vo.Email;
import cwchoiit.weolbuexam.domain.member.vo.PhoneNumber;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded private Email email;

    @Embedded private PhoneNumber phoneNumber;

    private String encodedPassword;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public static Member register(
            MemberRegisterPayload registerPayload, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.name = requireNonNull(registerPayload.name());
        member.email = new Email(requireNonNull(registerPayload.email()));
        member.phoneNumber = new PhoneNumber(requireNonNull(registerPayload.phoneNumber()));

        checkPassword(requireNonNull(registerPayload.rawPassword()));

        member.encodedPassword = passwordEncoder.encode(registerPayload.rawPassword());
        member.role = requireNonNull(registerPayload.role());

        return member;
    }

    public void changePassword(
            String prevPassword, String newPassword, PasswordEncoder passwordEncoder) {
        state(verifyPassword(prevPassword, passwordEncoder), "기존 비밀번호가 일치하지 않습니다");

        checkPassword(requireNonNull(newPassword));

        this.encodedPassword = passwordEncoder.encode(newPassword);
    }

    public boolean verifyPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.encodedPassword);
    }

    public boolean isInstructor() {
        return this.role == MemberRole.INSTRUCTOR;
    }

    public void changeRole(MemberRole newRole) {
        isTrue(this.role != newRole, "이미 변경하고자 하는 회원 유형입니다: " + newRole);

        this.role = newRole;
    }

    public void changePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = new PhoneNumber(requireNonNull(newPhoneNumber));
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
