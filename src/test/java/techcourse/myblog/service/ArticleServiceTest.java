package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ArticleServiceTest {
    private static final Long TEST_ID = 1L;
    private static final String TITLE = "title";
    private static final String COVER_URL = "coverUrl";
    private static final String CONTENTS = "Contents";
    private static final User AUTHOR = new User();

    private static final String TITLE_2 = "title2";
    private static final String COVER_URL_2 = "coverUrl2";
    private static final String CONTENTS_2 = "Contents2";

    @InjectMocks
    ArticleService articleService;

    @Mock
    ArticleRepository articleRepository;

    private InOrder inOrder;
    private Article testArticle;

    @BeforeEach
    void setUp() {
        inOrder = inOrder(articleRepository);
        testArticle = new Article(TITLE, COVER_URL, CONTENTS, AUTHOR);
    }

    @Test
    void article_저장_테스트() {
        ArticleDto testArticleDto = new ArticleDto();
        testArticleDto.setTitle(TITLE);
        testArticleDto.setCoverUrl(COVER_URL);
        testArticleDto.setContents(CONTENTS);
        testArticleDto.setAuthor(AUTHOR);

        articleService.save(testArticleDto);
        verify(articleRepository, atLeast(1)).save(testArticle);
    }

    @Test
    void ariticle_수정_테스트() {
        ArticleDto testArticleDto2 = new ArticleDto();
        testArticleDto2.setId(TEST_ID);
        testArticleDto2.setTitle(TITLE_2);
        testArticleDto2.setCoverUrl(COVER_URL_2);
        testArticleDto2.setContents(CONTENTS_2);
        testArticleDto2.setAuthor(AUTHOR);

        given(articleRepository.findById(TEST_ID)).willReturn(Optional.of(testArticle));
        Article updateArticle = articleService.update(testArticleDto2);

        verify(articleRepository, atLeast(1)).findById(TEST_ID);
        assertThat(updateArticle.getTitle()).isEqualTo(TITLE_2);
        assertThat(updateArticle.getCoverUrl()).isEqualTo(COVER_URL_2);
        assertThat(updateArticle.getContents()).isEqualTo(CONTENTS_2);
    }

}