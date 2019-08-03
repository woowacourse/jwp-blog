package techcourse.myblog.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.ArticleWriteService;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_DTO;
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
        article = ARTICLE_DTO.toArticle(author);
    }

    protected void compareArticle(Article article1, Article article2) {
        assertThat(article1.getTitle()).isEqualTo(article2.getTitle());
        assertThat(article1.getCoverUrl()).isEqualTo(article2.getCoverUrl());
        assertThat(article1.getContents()).isEqualTo(article2.getContents());
    }
}
