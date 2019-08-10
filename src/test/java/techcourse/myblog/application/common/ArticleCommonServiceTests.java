package techcourse.myblog.application.common;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.application.ArticleReadService;
import techcourse.myblog.application.ArticleWriteService;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_FEATURE;
import static techcourse.myblog.utils.UserTestObjects.AUTHOR_DTO;

public class ArticleCommonServiceTests {
    @Mock
    protected ArticleRepository articleRepository;

    protected ArticleWriteService articleWriteService;
    protected ArticleReadService articleReadService;

    protected Article article;
    protected User author;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        articleReadService = new ArticleReadService(articleRepository);
        articleWriteService = new ArticleWriteService(articleRepository, articleReadService);
        author = AUTHOR_DTO.toUser();
        article = ARTICLE_FEATURE.toArticle(author);
    }

    protected void compareArticle(Article article1, Article article2) {
        assertThat(article1.getArticleFeature().getTitle()).isEqualTo(article2.getArticleFeature().getTitle());
        assertThat(article1.getArticleFeature().getCoverUrl()).isEqualTo(article2.getArticleFeature().getCoverUrl());
        assertThat(article1.getArticleFeature()).isEqualTo(article2.getArticleFeature());
    }
}
