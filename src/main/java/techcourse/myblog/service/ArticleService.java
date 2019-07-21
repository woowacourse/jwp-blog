package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepo;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleRequestDto;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepo articleRepository;

    @Autowired
    public ArticleService(ArticleRepo articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void editArticle(Long articleId, ArticleRequestDto articleRequestDto) {
        Article articleToChange = findById(articleId);

        articleToChange.update(articleRequestDto);
    }

    public void addArticle(ArticleRequestDto articleRequestDto) {
        articleRepository.save(Article.of(articleRequestDto));
    }

    public void deleteById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public List<Article> findAll() {
//        return articleRepository.findAll();
        return null;
    }

    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));
    }
}
