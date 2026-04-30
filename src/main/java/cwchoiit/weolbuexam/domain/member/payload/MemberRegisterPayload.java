package cwchoiit.weolbuexam.domain.member.payload;

import cwchoiit.weolbuexam.domain.member.MemberRole;

public record MemberRegisterPayload(
        String name, String email, String phoneNumber, String rawPassword, MemberRole role) {}
