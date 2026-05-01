package cwchoiit.weolbuexam.domain.course;

import cwchoiit.weolbuexam.domain.BaseEntity;
import cwchoiit.weolbuexam.domain.course.payload.CourseOpenPayload;
import cwchoiit.weolbuexam.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer capacity;

    private Integer price;

    private int enrollCount;

    @JoinColumn(name = "instructor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member instructor;

    public static Course open(CourseOpenPayload openPayload, Member instructor) {
        Course course = new Course();
        course.title = requireNonNull(openPayload.title());
        course.capacity = requireNonNull(openPayload.capacity());
        course.price = requireNonNull(openPayload.price());

        checkInstructorRole(requireNonNull(instructor));

        course.instructor = instructor;

        return course;
    }

    public void increaseEnrollCount() {
        state(capacity > enrollCount, "최대 수강 인원을 초과했습니다");

        this.enrollCount++;
    }

    private static void checkInstructorRole(Member instructor) {
        state(instructor.isInstructor(), "강사만 강의를 등록할 수 있습니다");
    }
}
