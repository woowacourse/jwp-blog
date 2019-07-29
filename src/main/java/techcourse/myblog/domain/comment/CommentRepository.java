package techcourse.myblog.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.article.Article;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticle(Article article);
}
