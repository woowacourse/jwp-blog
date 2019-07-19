package techcourse.myblog.repo;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.Article;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}
