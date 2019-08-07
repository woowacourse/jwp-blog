package techcourse.myblog.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticleOrderByCreatedAt(Article article);
    void deleteByArticle(Article article);
}