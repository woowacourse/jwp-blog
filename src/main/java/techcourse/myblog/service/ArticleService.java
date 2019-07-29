package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

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
        return articleRepository.findById(articleId).map(ifExists -> {
                                                            toUpdate.setId(articleId);
                                                            articleRepository.save(toUpdate);
                                                            return true;
                                                        }).orElse(false);
    }

    public void delete(long articleId) {
        articleRepository.deleteById(articleId);
    }
}