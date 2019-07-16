package techcourse.myblog.repository;

import techcourse.myblog.domain.Article;

import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}
