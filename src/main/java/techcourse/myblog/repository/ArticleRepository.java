package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
