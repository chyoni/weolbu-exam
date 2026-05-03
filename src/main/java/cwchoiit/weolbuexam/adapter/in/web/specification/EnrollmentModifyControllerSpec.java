package cwchoiit.weolbuexam.adapter.in.web.specification;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.request.EnrollmentApplyRequest;
import cwchoiit.weolbuexam.adapter.in.web.response.EnrollmentApplyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "수강신청 API")
public interface EnrollmentModifyControllerSpec {

    @Operation(
            summary = "수강 신청 API",
            requestBody =
                    @RequestBody(
                            content = {
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = {
                                            @ExampleObject(name = "수강신청 데이터", value = APPLY_PAYLOAD)
                                        })
                            }))
    ResponseEntity<ApiResponse<List<EnrollmentApplyResponse>>> apply(
            EnrollmentApplyRequest request);

    String APPLY_PAYLOAD =
            """
            {
                "applicantId": 1,
                "courseIds": [1, 2, 3]
            }
            """;
}
