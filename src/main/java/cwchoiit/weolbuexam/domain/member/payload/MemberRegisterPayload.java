package cwchoiit.weolbuexam.domain.member.payload;

import cwchoiit.weolbuexam.domain.member.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberRegisterPayload(
        @Size(min = 3, max = 50) String name,
        @Email String email,
        @Size(min = 10, max = 20) String phoneNumber,
        @Size(min = 6, max = 10) String rawPassword,
        @NotNull MemberRole role) {}
