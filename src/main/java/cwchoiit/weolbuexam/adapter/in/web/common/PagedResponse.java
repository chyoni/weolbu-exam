package cwchoiit.weolbuexam.adapter.in.web.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

@Schema(description = "페이징 응답")
public record PagedResponse<T>(
        @Schema(description = "데이터 목록") List<T> content,
        @Schema(description = "현재 페이지 (0부터 시작)") int page,
        @Schema(description = "페이지당 항목 수") int size,
        @Schema(description = "전체 항목 수") long totalElements,
        @Schema(description = "전체 페이지 수") int totalPages) {

    public static <T> PagedResponse<T> of(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
