package cwchoiit.weolbuexam.application.member;

import cwchoiit.weolbuexam.application.provided.MemberLoadUseCase;
import cwchoiit.weolbuexam.application.provided.MemberRegisterUseCase;
import cwchoiit.weolbuexam.application.provided.MemberUpdateUseCase;
import cwchoiit.weolbuexam.application.required.MemberRepository;
import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import cwchoiit.weolbuexam.domain.member.PasswordEncoder;
import cwchoiit.weolbuexam.domain.member.payload.MemberRegisterPayload;
import cwchoiit.weolbuexam.domain.member.vo.Email;
import cwchoiit.weolbuexam.domain.member.vo.PhoneNumber;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegisterUseCase, MemberUpdateUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberLoadUseCase memberLoadUseCase;

    @Override
    public Member register(@Valid MemberRegisterPayload registerPayload) {
        checkDuplicateEmail(registerPayload.email());
        checkDuplicatePhoneNumber(registerPayload.phoneNumber());

        Member member = Member.register(registerPayload, passwordEncoder);

        return memberRepository.save(member);
    }

    @Override
    public void changePhoneNumber(Long memberId, String phoneNumber) {
        checkDuplicatePhoneNumber(phoneNumber);

        Member findMember = memberLoadUseCase.find(memberId);

        findMember.changePhoneNumber(phoneNumber);

        memberRepository.save(findMember);
    }

    @Override
    public void changePassword(Long memberId, String prevPassword, String newPassword) {
        Member findMember = memberLoadUseCase.find(memberId);

        findMember.changePassword(prevPassword, newPassword, passwordEncoder);

        memberRepository.save(findMember);
    }

    @Override
    public void changeRole(Long memberId, MemberRole role) {
        Member findMember = memberLoadUseCase.find(memberId);

        findMember.changeRole(role);

        memberRepository.save(findMember);
    }

    private void checkDuplicatePhoneNumber(String phoneNumber) {
        if (memberRepository.findByPhoneNumber(new PhoneNumber(phoneNumber)).isPresent()) {
            throw new IllegalArgumentException("동일한 핸드폰번호의 회원이 존재합니다");
        }
    }

    private void checkDuplicateEmail(String email) {
        if (memberRepository.findByEmail(new Email(email)).isPresent()) {
            throw new IllegalArgumentException("동일한 이메일의 회원이 존재합니다");
        }
    }
}
