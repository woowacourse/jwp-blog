package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.article.exception.MismatchAuthorException;
import techcourse.myblog.application.exception.NotFoundArticleException;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleReadService {
    private final ArticleRepository articleRepository;

    public ArticleReadService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return Collections.unmodifiableList(articleRepository.findAll());
    }

    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
    }

    public Article findByIdAndAuthor(Long articleId, User user) {
        return articleRepository.findByIdAndAuthor(articleId, user)
                .orElseThrow(MismatchAuthorException::new);
    }
}
