package cwchoiit.weolbuexam.domain.member;

import cwchoiit.weolbuexam.adapter.out.security.SecurePasswordEncoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderTest {

    PasswordEncoder passwordEncoder = new SecurePasswordEncoder();

    @Test
    void encode() {
        String rawPassword = "secret";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(encodedPassword).isNotEqualTo(rawPassword);
    }

    @Test
    void matches() {
        String rawPassword = "secret";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}
