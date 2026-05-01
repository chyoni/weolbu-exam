package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.domain.member.Member;

/** 회원을 조회한다 */
public interface MemberLoadUseCase {
    Member find(Long memberId);
}
