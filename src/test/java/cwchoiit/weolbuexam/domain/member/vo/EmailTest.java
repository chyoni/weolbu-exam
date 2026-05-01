package cwchoiit.weolbuexam.domain.member.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void constructor() {
        Email email = new Email("noreply@example.com");
        assertThat(email.address()).isEqualTo("noreply@example.com");
    }

    @Test
    void validateFail() {
        assertThatThrownBy(() -> new Email("invalid")).isInstanceOf(IllegalArgumentException.class);
    }
}
