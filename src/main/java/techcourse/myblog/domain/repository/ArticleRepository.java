package techcourse.myblog.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
