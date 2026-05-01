package cwchoiit.weolbuexam.adapter.in.web;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.request.MemberRegisterRequest;
import cwchoiit.weolbuexam.adapter.in.web.request.MemberUpdatePasswordRequest;
import cwchoiit.weolbuexam.adapter.in.web.response.MemberRegisterResponse;
import cwchoiit.weolbuexam.domain.member.MemberRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 생성 및 수정 API")
public interface MemberModifyControllerSpec {

    @Operation(
            summary = "회원 가입 API",
            requestBody =
                    @RequestBody(
                            content = {
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = {
                                            @ExampleObject(
                                                    name = "회원 가입 데이터",
                                                    value = REGISTER_PAYLOAD)
                                        })
                            }))
    ResponseEntity<ApiResponse<MemberRegisterResponse>> register(
            MemberRegisterRequest memberRegisterRequest);

    @Operation(summary = "핸드폰 번호 변경 API")
    ResponseEntity<ApiResponse<Void>> changePhoneNumber(
            @Parameter(example = "1") Long memberId,
            @Parameter(example = "01011112222") String phoneNumber);

    @Operation(
            summary = "비밀번호 변경 API",
            requestBody =
                    @RequestBody(
                            content = {
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = {
                                            @ExampleObject(
                                                    name = "비밀번호 변경 데이터",
                                                    value = UPDATE_PASSWORD_PAYLOAD)
                                        })
                            }))
    ResponseEntity<ApiResponse<Void>> changePassword(
            @Parameter(example = "1") Long memberId,
            MemberUpdatePasswordRequest memberUpdatePasswordRequest);

    @Operation(summary = "회원 역할 변경 API")
    ResponseEntity<ApiResponse<Void>> changeRole(
            @Parameter(example = "1") Long memberId,
            @Parameter(example = "INSTRUCTOR") MemberRole role);

    String REGISTER_PAYLOAD =
            """
            {
                "name": "홍길동",
                "email": "noreply@example.com",
                "phoneNumber": "01011112222",
                "rawPassword": "Secret123",
                "role": "STUDENT"
            }
            """;

    String UPDATE_PASSWORD_PAYLOAD =
            """
            {
                "prevPassword": "Secret",
                "newPassword": "newSecret"
            }
            """;
}
