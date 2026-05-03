package cwchoiit.weolbuexam.adapter.in.web;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.request.EnrollmentApplyRequest;
import cwchoiit.weolbuexam.adapter.in.web.response.EnrollmentApplyResponse;
import cwchoiit.weolbuexam.adapter.in.web.specification.EnrollmentModifyControllerSpec;
import cwchoiit.weolbuexam.application.provided.EnrollmentApplyUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/enrollments")
public class EnrollmentModifyController implements EnrollmentModifyControllerSpec {

    private final EnrollmentApplyUseCase enrollmentApplyUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<List<EnrollmentApplyResponse>>> apply(
            @RequestBody EnrollmentApplyRequest request) {
        List<EnrollmentApplyResponse> responses =
                enrollmentApplyUseCase.apply(request.toPayload(), request.applicantId()).stream()
                        .map(EnrollmentApplyResponse::of)
                        .toList();
        return ResponseEntity.status(202).body(ApiResponse.ok(responses));
    }
}
