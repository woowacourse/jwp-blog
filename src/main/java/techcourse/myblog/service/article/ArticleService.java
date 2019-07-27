package techcourse.myblog.service.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.presentation.ArticleRepository;
import techcourse.myblog.service.dto.article.ArticleDto;
import techcourse.myblog.exception.ArticleNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static techcourse.myblog.service.article.ArticleAssembler.convertToEntity;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleDto> findAll() {
        List<Article> articles = articleRepository.findAll();
        return Collections.unmodifiableList(articles.stream()
                .map(ArticleAssembler::convertToDto)
                .collect(Collectors.toList()));
    }

    public ArticleDto findById(final Long id) {
        return articleRepository.findById(Objects.requireNonNull(id))
                .map(ArticleAssembler::convertToDto)
                .orElseThrow(ArticleNotFoundException::new);
    }

    public Long save(final ArticleDto articleDTO) {
        Article article = convertToEntity(Objects.requireNonNull(articleDTO));
        Article persistArticle = articleRepository.save(article);
        return persistArticle.getId();
    }

    @Transactional
    public void update(final Long id, final ArticleDto articleDTO) {
        Article article = convertToEntity(Objects.requireNonNull(articleDTO));
        articleRepository.findById(Objects.requireNonNull(id))
                .ifPresent((retrieveArticle -> retrieveArticle.update(article)));
    }

    public void delete(final Long id) {
        articleRepository.deleteById(Objects.requireNonNull(id));
    }
}
