package cwchoiit.weolbuexam.adapter.in.web.response;

import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 조회 응답")
public record MemberLoadResponse(
        @Schema(description = "회원 ID") Long memberId,
        @Schema(description = "이름") String name,
        @Schema(description = "이메일") String email,
        @Schema(description = "전화번호") String phoneNumber,
        @Schema(description = "권한") MemberRole role) {

    public static MemberLoadResponse of(Member member) {
        return new MemberLoadResponse(
                member.getId(),
                member.getName(),
                member.getEmail().address(),
                member.getPhoneNumber().value(),
                member.getRole());
    }
}
