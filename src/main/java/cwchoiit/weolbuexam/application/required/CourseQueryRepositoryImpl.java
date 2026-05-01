package cwchoiit.weolbuexam.application.required;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cwchoiit.weolbuexam.domain.course.Course;
import cwchoiit.weolbuexam.domain.course.QCourse;
import cwchoiit.weolbuexam.domain.course.payload.CourseSearchPayload;
import cwchoiit.weolbuexam.domain.member.QMember;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CourseQueryRepositoryImpl implements CourseQueryRepository {

    private static final QCourse course = QCourse.course;
    private static final QMember member = QMember.member;

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Course> findCourses(CourseSearchPayload payload) {
        List<Course> content =
                queryFactory
                        .selectFrom(course)
                        .leftJoin(course.instructor, member)
                        .fetchJoin()
                        .orderBy(orderSpecifiers(payload))
                        .offset((long) payload.page() * payload.size())
                        .limit(payload.size())
                        .fetch();

        Long total = queryFactory.select(course.count()).from(course).fetchOne();

        return new PageImpl<>(
                content, PageRequest.of(payload.page(), payload.size()), total == null ? 0 : total);
    }

    private OrderSpecifier<?>[] orderSpecifiers(CourseSearchPayload payload) {
        return switch (payload.sort()) {
            case ENROLL_COUNT_DESC ->
                    new OrderSpecifier<?>[] {course.enrollCount.desc(), course.id.desc()};
            case ENROLL_RATE_DESC -> {
                NumberExpression<Double> enrollRate =
                        course.enrollCount.doubleValue().divide(course.capacity.doubleValue());
                yield new OrderSpecifier<?>[] {enrollRate.desc(), course.id.desc()};
            }
            default -> new OrderSpecifier<?>[] {course.createdAt.desc(), course.id.desc()};
        };
    }
}
