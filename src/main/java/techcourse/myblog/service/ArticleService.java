package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(final Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Article save(final Article article) {
        return articleRepository.save(article);
    }

    public Article update(final Long articleId, final ArticleDto articleDto) {
        final Article selectedArticle = findById(articleId);
        return articleRepository.save(selectedArticle.update(articleDto));
    }

    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
