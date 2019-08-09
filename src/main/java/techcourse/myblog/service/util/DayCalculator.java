package techcourse.myblog.service.util;

import techcourse.myblog.domain.comment.Comment;

import java.time.LocalDate;
import java.util.function.Function;

public class DayCalculator implements Function<Comment, Integer> {
    @Override
    public Integer apply(Comment comment) {
        LocalDate now = LocalDate.now();
        LocalDate commentCreatedDay = comment.toCreatedDate();
        return now.getDayOfYear() - commentCreatedDay.getDayOfYear();
    }
}
