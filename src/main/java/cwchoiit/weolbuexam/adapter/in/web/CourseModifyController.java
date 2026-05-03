package cwchoiit.weolbuexam.adapter.in.web;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.request.CourseOpenRequest;
import cwchoiit.weolbuexam.adapter.in.web.response.CourseOpenResponse;
import cwchoiit.weolbuexam.application.provided.CourseOpenUseCase;
import cwchoiit.weolbuexam.domain.course.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseModifyController implements CourseModifyControllerSpec {

    private final CourseOpenUseCase courseOpenUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<CourseOpenResponse>> open(
            @RequestBody CourseOpenRequest request) {
        Course course = courseOpenUseCase.open(request.toPayload(), request.instructorId());
        return ResponseEntity.status(202).body(ApiResponse.ok(CourseOpenResponse.of(course)));
    }
}
