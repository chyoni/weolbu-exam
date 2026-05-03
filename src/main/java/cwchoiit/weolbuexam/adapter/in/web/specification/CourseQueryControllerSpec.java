package cwchoiit.weolbuexam.adapter.in.web.specification;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.common.PagedResponse;
import cwchoiit.weolbuexam.adapter.in.web.response.CourseListItemResponse;
import cwchoiit.weolbuexam.domain.course.payload.CourseSortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "강의 조회 관련 API")
public interface CourseQueryControllerSpec {

    @Operation(summary = "강의 목록 조회 API")
    ResponseEntity<ApiResponse<PagedResponse<CourseListItemResponse>>> findCourses(
            @Parameter(
                            description =
                                    "정렬 기준 (CREATED_AT_DESC: 최근 등록순, ENROLL_COUNT_DESC: 신청자 많은 순, ENROLL_RATE_DESC: 신청률 높은 순)",
                            example = "CREATED_AT_DESC")
                    CourseSortType sort,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") int page,
            @Parameter(description = "페이지 크기 (기본값 20)", example = "20") int size);
}
