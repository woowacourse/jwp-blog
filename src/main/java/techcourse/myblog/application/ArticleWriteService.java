package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.article.Feature;
import techcourse.myblog.domain.user.User;

@Service
@Transactional
public class ArticleWriteService {
    private final ArticleRepository articleRepository;
    private final ArticleReadService articleReadService;

    public ArticleWriteService(ArticleRepository articleRepository, ArticleReadService articleReadService) {
        this.articleRepository = articleRepository;
        this.articleReadService = articleReadService;
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void update(Long articleId, Feature feature, User user) {
        articleReadService.findById(articleId).update(feature.toArticle(user));
    }
}
