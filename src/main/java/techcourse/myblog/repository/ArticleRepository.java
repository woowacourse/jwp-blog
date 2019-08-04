package techcourse.myblog.repository;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.article.Article;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}

