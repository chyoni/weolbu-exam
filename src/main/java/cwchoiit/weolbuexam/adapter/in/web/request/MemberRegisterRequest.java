package cwchoiit.weolbuexam.adapter.in.web.request;

import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 가입 요청")
public record MemberRegisterRequest(
        @Schema(description = "이름") String name,
        @Schema(description = "이메일") String email,
        @Schema(description = "핸드폰 번호") String phoneNumber,
        @Schema(description = "비밀번호") String rawPassword,
        @Schema(description = "회원 유형") MemberRole role) {

    public MemberRegisterPayload toPayload() {
        return new MemberRegisterPayload(name, email, phoneNumber, rawPassword, role);
    }
}
