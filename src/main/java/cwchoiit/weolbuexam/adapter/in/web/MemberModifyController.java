package cwchoiit.weolbuexam.adapter.in.web;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.request.MemberRegisterRequest;
import cwchoiit.weolbuexam.adapter.in.web.request.MemberUpdatePasswordRequest;
import cwchoiit.weolbuexam.adapter.in.web.response.MemberRegisterResponse;
import cwchoiit.weolbuexam.adapter.in.web.specification.MemberModifyControllerSpec;
import cwchoiit.weolbuexam.application.provided.MemberRegisterUseCase;
import cwchoiit.weolbuexam.application.provided.MemberUpdateUseCase;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberModifyController implements MemberModifyControllerSpec {

    private final MemberRegisterUseCase memberRegisterUseCase;
    private final MemberUpdateUseCase memberUpdateUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<MemberRegisterResponse>> register(
            @RequestBody MemberRegisterRequest memberRegisterRequest) {
        MemberRegisterResponse memberRegisterResponse =
                MemberRegisterResponse.of(
                        memberRegisterUseCase.register(memberRegisterRequest.toPayload()));
        return ResponseEntity.status(202).body(ApiResponse.ok(memberRegisterResponse));
    }

    @Override
    @PutMapping("/{memberId}/phone")
    public ResponseEntity<ApiResponse<Void>> changePhoneNumber(
            @PathVariable Long memberId, String phoneNumber) {
        memberUpdateUseCase.changePhoneNumber(memberId, phoneNumber);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Override
    @PutMapping("/{memberId}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long memberId,
            @RequestBody MemberUpdatePasswordRequest memberUpdatePasswordRequest) {
        memberUpdateUseCase.changePassword(
                memberId,
                memberUpdatePasswordRequest.prevPassword(),
                memberUpdatePasswordRequest.newPassword());
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Override
    @PutMapping("/{memberId}/role")
    public ResponseEntity<ApiResponse<Void>> changeRole(
            @PathVariable Long memberId, MemberRole role) {
        memberUpdateUseCase.changeRole(memberId, role);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
