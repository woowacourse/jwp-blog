package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    @Transactional
    default void update(int id, ArticleDto articleDto) {
        Article article = findById(id).get();
        article.update(articleDto.toArticle());
    }
}
