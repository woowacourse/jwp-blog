package techcourse.myblog.comment.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
