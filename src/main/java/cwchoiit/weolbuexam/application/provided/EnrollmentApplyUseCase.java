package cwchoiit.weolbuexam.application.provided;

import cwchoiit.weolbuexam.domain.enrollment.Enrollment;
import cwchoiit.weolbuexam.domain.enrollment.payload.EnrollmentApplyPayload;
import jakarta.validation.Valid;
import java.util.List;

/** 수강을 신청한다 */
public interface EnrollmentApplyUseCase {
    List<Enrollment> apply(@Valid EnrollmentApplyPayload payload, Long applicantId);
}
