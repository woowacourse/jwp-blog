package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.ArticleResponseDto;
import techcourse.myblog.application.exception.NotFoundArticleException;
import techcourse.myblog.domain.article.ArticleFeature;
import techcourse.myblog.domain.article.ArticleRepository;
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

    public ArticleResponseDto save(ArticleFeature articleFeature, User user) {
        return ArticleAssembler.buildArticleResponseDto(articleRepository.save(articleFeature.toArticle(user)));
    }

    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void update(Long articleId, ArticleFeature articleFeature, User user) {
        articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new)
                .update(articleFeature.toArticle(user));
    }
}
