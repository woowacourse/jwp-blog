package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;

import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Optional<Article> maybeArticle(long articleId) {
        return articleRepository.findById(articleId);
    }

    public Iterable<Article> loadEveryArticles() {
        return articleRepository.findAll();
    }

    public long write(Article toWrite) {
        return articleRepository.save(toWrite).getId();
    }

    @Transactional
    public void tryUpdate(long articleId, Article toUpdate) {
        articleRepository.findById(articleId).filter(article -> article.isSameAuthor(toUpdate))
                                            .ifPresent(original -> original.update(toUpdate));
    }

    @Transactional
    public void tryDelete(long articleId, User user) {
        articleRepository.findById(articleId).filter(article -> article.isSameAuthor(user))
                                            .ifPresent(article -> articleRepository.deleteById(articleId));
    }
}