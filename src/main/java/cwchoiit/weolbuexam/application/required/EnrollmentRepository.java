package cwchoiit.weolbuexam.application.required;

import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.enrollment.Enrollment;
import cwchoiit.weolbuexam.domain.member.Member;
import org.springframework.data.repository.Repository;

public interface EnrollmentRepository extends Repository<Enrollment, Long> {
    Enrollment save(Enrollment enrollment);

    boolean existsByApplicantAndCourse(Member applicant, Course course);
}
