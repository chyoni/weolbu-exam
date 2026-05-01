package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.domain.member.MemberRole;

public interface MemberUpdateUseCase {
    void changePhoneNumber(Long memberId, String phoneNumber);

    void changePassword(Long memberId, String prevPassword, String newPassword);

    void changeRole(Long memberId, MemberRole role);
}
