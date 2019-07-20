package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.service.dto.ArticleRequest;

import javax.transaction.Transactional;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Iterable<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article post(ArticleRequest articleRequest) {
        Article article = new Article(articleRequest.getTitle(),
                articleRequest.getCoverUrl(), articleRequest.getContents());
        return articleRepository.save(article);
    }

    public Article findById(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public Article editArticle(ArticleRequest articleRequest, long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.updateArticle(articleRequest.toArticle());
        return article;
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
