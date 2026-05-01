package cwchoiit.weolbuexam.domain.course.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseOpenPayload(
        @Size(min = 3, max = 200) String title, @NotNull Integer capacity, @NotNull Long price) {}
