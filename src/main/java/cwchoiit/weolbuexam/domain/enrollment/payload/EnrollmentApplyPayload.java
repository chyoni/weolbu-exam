package cwchoiit.weolbuexam.domain.enrollment.payload;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record EnrollmentApplyPayload(@NotEmpty List<Long> courseIds) {}
