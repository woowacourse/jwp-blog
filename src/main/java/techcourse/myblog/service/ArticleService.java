package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleRequestDto;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void editArticle(Long articleId, ArticleRequestDto articleRequestDto) {
        Article articleToChange = findById(articleId);

        articleToChange.update(articleRequestDto);
    }

    public void addArticle(ArticleRequestDto articleRequestDto) {
        articleRepository.addArticle(Article.of(articleRequestDto));
    }

    public void deleteById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));
    }
}
