package cwchoiit.weolbuexam.domain.course.payload;

public record CourseSearchPayload(CourseSortType sort, int page, int size) {
    public CourseSearchPayload {
        if (sort == null) sort = CourseSortType.CREATED_AT_DESC;
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 20;
    }
}
