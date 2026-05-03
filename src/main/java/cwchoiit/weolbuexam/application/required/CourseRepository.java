package cwchoiit.weolbuexam.application.required;

import cwchoiit.weolbuexam.domain.course.Course;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends Repository<Course, Long>, CourseQueryRepository {
    Course save(Course course);

    Optional<Course> findById(Long courseId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Course c where c.id = :id")
    Optional<Course> findByIdForUpdate(@Param("id") Long id);
}
