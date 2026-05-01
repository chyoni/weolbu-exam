package cwchoiit.weolbuexam.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 패스워드 변경 요청")
public record MemberUpdatePasswordRequest(
        @Schema(description = "현재 비밀번호") String prevPassword,
        @Schema(description = "변경할 비밀번호") String newPassword) {}
