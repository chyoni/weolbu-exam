package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.domain.member.MemberRole;

/** 회원 정보를 수정한다 */
public interface MemberUpdateUseCase {
    void changePhoneNumber(Long memberId, String phoneNumber);

    void changePassword(Long memberId, String prevPassword, String newPassword);

    void changeRole(Long memberId, MemberRole role);
}
