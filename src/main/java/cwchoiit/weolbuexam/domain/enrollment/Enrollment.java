package cwchoiit.weolbuexam.domain.enrollment;

import static java.util.Objects.requireNonNull;

import cwchoiit.weolbuexam.domain.BaseEntity;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.member.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member applicant;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    private LocalDateTime enrolledAt;

    public static Enrollment apply(Course course, Member applicant) {
        requireNonNull(course);
        requireNonNull(applicant);
        course.increaseEnrollCount();

        Enrollment enrollment = new Enrollment();
        enrollment.course = course;
        enrollment.applicant = applicant;
        enrollment.enrolledAt = LocalDateTime.now();
        return enrollment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment enrollment = (Enrollment) o;
        return id != null && id.equals(enrollment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
