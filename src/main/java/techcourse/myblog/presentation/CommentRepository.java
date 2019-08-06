package techcourse.myblog.presentation;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);
}
