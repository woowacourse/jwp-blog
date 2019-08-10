package techcourse.myblog.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.comment.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticle(Article article);
}
