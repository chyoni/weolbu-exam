package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import jakarta.validation.Valid;

/** 회원 가입 */
public interface MemberRegisterUseCase {
    Member register(@Valid MemberRegisterPayload registerPayload);
}
