package cwchoiit.weolbuexam.adapter.in.web;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.common.PagedResponse;
import cwchoiit.weolbuexam.adapter.in.web.response.CourseListItemResponse;
import cwchoiit.weolbuexam.adapter.in.web.specification.CourseQueryControllerSpec;
import cwchoiit.weolbuexam.application.provided.CourseLoadUseCase;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.payload.CourseSearchPayload;
import cwchoiit.weolbuexam.domain.course.payload.CourseSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseQueryController implements CourseQueryControllerSpec {

    private final CourseLoadUseCase courseLoadUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CourseListItemResponse>>> findCourses(
            @RequestParam(defaultValue = "CREATED_AT_DESC") CourseSortType sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Course> courses =
                courseLoadUseCase.loadCourses(new CourseSearchPayload(sort, page, size));
        Page<CourseListItemResponse> responses = courses.map(CourseListItemResponse::of);
        return ResponseEntity.ok(ApiResponse.ok(PagedResponse.of(responses)));
    }
}
