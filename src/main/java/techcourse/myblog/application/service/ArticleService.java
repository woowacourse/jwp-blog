package techcourse.myblog.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.User;

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
    public boolean tryUpdate(long articleId, Article toUpdate) {
        return articleRepository.findById(articleId).map(original -> {
                                                            if (original.isSameAuthor(toUpdate)) {
                                                                original.update(toUpdate);
                                                                return true;
                                                            }
                                                            return false;
                                                        }).orElse(false);
    }

    @Transactional
    public void tryDelete(long articleId, User user) {
        articleRepository.findById(articleId).ifPresent(article -> {
            if (article.isSameAuthor(user)) {
                articleRepository.deleteById(articleId);
            }
        });
    }
}