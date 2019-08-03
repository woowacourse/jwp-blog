package techcourse.myblog.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}