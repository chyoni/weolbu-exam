package cwchoiit.weolbuexam.domain.member;

public interface PasswordEncoder {
    String encode(String rawPassword);

    boolean matches(String rawPassword);
}
