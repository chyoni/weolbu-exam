package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.domain.member.Member;

public interface MemberLoadUseCase {
    Member find(Long memberId);
}
