package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.ArticleRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class ArticleServiceTest {
    private static final Long TEST_ID = 1L;
    private static final String TITLE = "title";
    private static final String COVER_URL = "coverUrl";
    private static final String CONTENTS = "Contents";

    private static final String TITLE_2 = "title2";
    private static final String COVER_URL_2 = "coverUrl2";
    private static final String CONTENTS_2 = "Contents2";

    private static final User USER = new User("test", "test@test.com", "passWord!1");

    @InjectMocks
    ArticleService articleService;

    @Mock
    ArticleRepository articleRepository;

    private Article testArticle;
    private ArticleDto testArticleDto;

    @BeforeEach
    void setUp() {
        testArticle = new Article(TITLE, COVER_URL, CONTENTS, USER);
        testArticleDto = new ArticleDto();

        testArticleDto.setTitle(TITLE);
        testArticleDto.setCoverUrl(COVER_URL);
        testArticleDto.setContents(CONTENTS);
    }

    @Test
    void 조회테스트() {
        given(articleRepository.findById(TEST_ID)).willReturn(Optional.of(testArticle));
        articleService.findById(TEST_ID);

        verify(articleRepository, atLeast(1)).findById(TEST_ID);
    }

    @Test
    void article_저장_테스트() {
        ArticleDto testArticleDto = new ArticleDto();
        testArticleDto.setTitle(TITLE);
        testArticleDto.setCoverUrl(COVER_URL);
        testArticleDto.setContents(CONTENTS);

        articleService.save(testArticleDto, USER);
        verify(articleRepository, atLeast(1)).save(testArticle);
    }

    @Test
    void ariticle_수정_테스트() {
        ArticleDto testArticleDto2 = new ArticleDto();
        testArticleDto2.setId(TEST_ID);
        testArticleDto2.setTitle(TITLE_2);
        testArticleDto2.setCoverUrl(COVER_URL_2);
        testArticleDto2.setContents(CONTENTS_2);

        given(articleRepository.findById(TEST_ID)).willReturn(Optional.of(testArticle));
        Article updateArticle = articleService.update(testArticleDto2, USER);

        verify(articleRepository, atLeast(1)).findById(TEST_ID);
        assertThat(updateArticle.getTitle()).isEqualTo(TITLE_2);
        assertThat(updateArticle.getCoverUrl()).isEqualTo(COVER_URL_2);
        assertThat(updateArticle.getContents()).isEqualTo(CONTENTS_2);
    }

    @Test
    @DisplayName("article 삭제 테스트")
    void delete() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(TITLE);
        articleDto.setCoverUrl(COVER_URL);
        articleDto.setContents(CONTENTS);

        articleService.delete(TEST_ID);

        verify(articleRepository, atLeast(1)).deleteById(TEST_ID);
    }
}