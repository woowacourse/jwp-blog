package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void editArticle(Long articleId, Article article) {
        Article articleToChange = findById(articleId);

        articleToChange.update(article);
    }

    public void addArticle(Article article) {
        articleRepository.addArticle(Article.of(article.getTitle(), article.getCoverUrl(), article.getContents(), article.getCategory()));
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
