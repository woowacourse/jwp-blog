package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.dto.ArticleDto;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Transactional
    default void update(Long id, ArticleDto articleDto) {
        Article article = findById(id).get();
        article.update(articleDto.toArticle());
    }
}
