package cwchoiit.weolbuexam.application.required;

import cwchoiit.weolbuexam.domain.member.Member;
import cwchoiit.weolbuexam.domain.member.vo.Email;
import cwchoiit.weolbuexam.domain.member.vo.PhoneNumber;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findByPhoneNumber(PhoneNumber phoneNumber);

    Optional<Member> findById(Long memberId);
}
