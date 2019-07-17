package techcourse.myblog.repository;

import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public interface ArticleRepository extends CrudRepository<Article,Long> {

}
