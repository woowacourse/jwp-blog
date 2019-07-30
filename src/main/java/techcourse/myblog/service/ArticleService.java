package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
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

    public boolean tryUpdate(long articleId, Article toUpdate) {
        return articleRepository.findById(articleId).map(original -> {
                                                            if (original.isSameAuthor(toUpdate)) {
                                                                toUpdate.setId(articleId);
                                                                articleRepository.save(toUpdate);
                                                                return true;
                                                            }
                                                            return false;
                                                        }).orElse(false);
    }

    public void tryDelete(long articleId, User user) {
        articleRepository.findById(articleId).ifPresent(article -> {
            if (article.isSameAuthor(user)) {
                articleRepository.deleteById(articleId);
            }
        });
    }
}