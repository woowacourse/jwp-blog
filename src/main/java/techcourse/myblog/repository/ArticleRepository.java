package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByIdAndAuthor(Long articleId, User author);
}