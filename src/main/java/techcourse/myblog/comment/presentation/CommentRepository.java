package techcourse.myblog.comment.presentation;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);
}
