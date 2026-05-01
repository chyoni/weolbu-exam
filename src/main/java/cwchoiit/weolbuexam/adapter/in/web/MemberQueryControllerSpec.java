package cwchoiit.weolbuexam.adapter.in.web;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.response.MemberLoadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 조회 관련 API")
public interface MemberQueryControllerSpec {

    @Operation(summary = "회원 조회 API")
    ResponseEntity<ApiResponse<MemberLoadResponse>> findMember(
            @Parameter(example = "1") Long memberId);
}
