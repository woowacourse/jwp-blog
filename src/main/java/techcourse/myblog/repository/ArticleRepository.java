package techcourse.myblog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import techcourse.myblog.domain.Article;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    Page<Article> findAll(Pageable pageable);
}