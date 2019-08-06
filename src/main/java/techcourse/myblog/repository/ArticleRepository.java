package techcourse.myblog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findAllByAuthor(User author);
}
