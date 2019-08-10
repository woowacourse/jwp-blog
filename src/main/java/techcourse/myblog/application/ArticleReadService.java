package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.exception.NotFoundArticleException;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;

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
}
