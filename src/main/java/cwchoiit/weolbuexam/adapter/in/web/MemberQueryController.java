package cwchoiit.weolbuexam.adapter.in.web;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.response.MemberLoadResponse;
import cwchoiit.weolbuexam.application.provided.MemberLoadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberQueryController implements MemberQueryControllerSpec {

    private final MemberLoadUseCase memberLoadUseCase;

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberLoadResponse>> findMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(
                ApiResponse.ok(MemberLoadResponse.of(memberLoadUseCase.find(memberId))));
    }
}
