package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.web.controller.dto.ArticleDto;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Article save(ArticleDto articleDto) {
        return articleRepository.save(Article.of(articleDto));
    }

    public Article update(Long articleId, ArticleDto articleDto) {
        Article selectedArticle = findById(articleId);
        return articleRepository.save(selectedArticle.update(articleDto));
    }

    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
