package techcourse.myblog.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import techcourse.myblog.domain.article.Article;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticle(Article article);

    @Query(value = "SELECT count(*) FROM comment c where c.article_id = :articleId", nativeQuery = true)
    int countByArticleId(@Param("articleId") Long articleId);
}
