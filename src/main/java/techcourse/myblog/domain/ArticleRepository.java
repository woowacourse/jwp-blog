package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    default void update(Long id, ArticleDto articleDto) {
        Article article = findById(id).get();
        article.update(articleDto.toArticle());
        save(article);
    }
}
