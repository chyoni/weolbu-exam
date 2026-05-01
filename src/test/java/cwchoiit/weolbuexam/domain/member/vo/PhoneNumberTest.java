package cwchoiit.weolbuexam.domain.member.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneNumberTest {

    @Test
    void constructor() {
        PhoneNumber phoneNumber = new PhoneNumber("01012345678");
        assertThat(phoneNumber.value()).isEqualTo("01012345678");
    }

    @Test
    void validateFail() {
        assertThatThrownBy(() -> new PhoneNumber("1234"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
