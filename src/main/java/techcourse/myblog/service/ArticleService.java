package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.exception.ArticleNotFoundException;
import techcourse.myblog.domain.repository.ArticleRepository;

import java.util.List;

import static java.util.Collections.unmodifiableList;

@Service
public class ArticleService {
    private static final String ARTICLE_ERROR = "해당하는 게시글을 찾지 못했습니다.";

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return unmodifiableList(articleRepository.findAll());
    }

    @Transactional
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public Article findById(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(ARTICLE_ERROR));
    }

    @Transactional
    public Article findById(long articleId, long loginUserId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(ARTICLE_ERROR));
        article.isAuthor(loginUserId);
        return article;
    }

    @Transactional
    public Article update(long id, Article articleToUpdate) {
        Article originArticle = articleRepository.findArticleById(id);
        long loginUserId = articleToUpdate.getAuthor().getId();
        originArticle.update(articleToUpdate, loginUserId);

        return originArticle;
    }

    @Transactional
    public void deleteById(long id) {
        articleRepository.deleteById(id);
    }
}
