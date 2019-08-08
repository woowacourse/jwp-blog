package techcourse.myblog.comment.presentation;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
