package cwchoiit.weolbuexam.adapter.in.web.specification;

import cwchoiit.weolbuexam.adapter.in.web.common.ApiResponse;
import cwchoiit.weolbuexam.adapter.in.web.request.CourseOpenRequest;
import cwchoiit.weolbuexam.adapter.in.web.response.CourseOpenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "강의 등록 관련 API")
public interface CourseModifyControllerSpec {

    @Operation(
            summary = "강의 등록 API",
            requestBody =
                    @RequestBody(
                            content = {
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = {
                                            @ExampleObject(name = "강의 등록 데이터", value = OPEN_PAYLOAD)
                                        })
                            }))
    ResponseEntity<ApiResponse<CourseOpenResponse>> open(CourseOpenRequest request);

    String OPEN_PAYLOAD =
            """
            {
                "instructorId": 1,
                "title": "너나위의 내집마련 기초반",
                "capacity": 30,
                "price": 200000
            }
            """;
}
