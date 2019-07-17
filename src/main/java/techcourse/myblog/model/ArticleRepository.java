package techcourse.myblog.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

}
