package techcourse.myblog.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.article.Article;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticle(Article article);
}
