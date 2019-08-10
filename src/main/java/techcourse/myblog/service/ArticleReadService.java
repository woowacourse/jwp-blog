package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.service.exception.NotFoundArticleException;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleReadService {
    private final ArticleRepository articleRepository;

    @Autowired
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
        Article article = findById(articleId);
        article.matchAuthor(user);
        return article;
    }
}
