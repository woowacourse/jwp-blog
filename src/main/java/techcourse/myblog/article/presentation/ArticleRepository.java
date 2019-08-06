package techcourse.myblog.article.presentation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techcourse.myblog.article.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
