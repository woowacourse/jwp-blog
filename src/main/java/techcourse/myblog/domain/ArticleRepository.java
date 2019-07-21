package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.service.dto.ArticleDto;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Transactional
    default Optional<Article> update(Long id, ArticleDto articleDto) {
        Optional<Article> articleOptional = findById(id);
        articleOptional.ifPresent(article -> article.update(articleDto.toArticle()));
        return findById(id);
    }
}
