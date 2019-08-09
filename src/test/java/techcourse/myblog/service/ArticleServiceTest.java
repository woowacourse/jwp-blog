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
import static techcourse.myblog.service.UserServiceTest.*;

@ExtendWith(SpringExtension.class)
class ArticleServiceTest {
    static final Long TEST_ID = 1L;
    static final String TITLE = "TITLE";
    static final String COVER_URL = "COVER_URL";
    static final String CONTENTS = "Contents";

    @InjectMocks
    ArticleService articleService;

    @Mock
    ArticleRepository articleRepository;

    private Article testArticle;
    private ArticleDto testArticleDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, EMAIL, PASSWORD);
        testArticle = new Article(TITLE, COVER_URL, CONTENTS, user);
        testArticleDto = new ArticleDto(TITLE, COVER_URL, CONTENTS, user.getEmail());
    }

    @Test
    @DisplayName("게시글을 저장한다.")
    void saveArticle() {
        articleService.save(testArticleDto, user);

        verify(articleRepository, atLeast(1)).save(testArticle);
    }

    @Test
    @DisplayName("게시글을 조회한다.")
    void fetchArticle() {
        given(articleRepository.findById(TEST_ID)).willReturn(Optional.of(testArticle));

        assertThat(articleService.findById(TEST_ID)).isEqualTo(testArticle);
    }

    @Test
    @DisplayName("게시글을 update한다")
    void updateArticle() {
        // Given
        final String updatedTitle = "title2";
        final String updatedCoverUrl = "coverUrl2";
        final String updatedContents = "Contents2";

        ArticleDto updatedArticleDto = new ArticleDto();

        given(articleRepository.findById(TEST_ID)).willReturn(Optional.of(testArticle));

        // When
        updatedArticleDto.setId(TEST_ID);
        updatedArticleDto.setTitle(updatedTitle);
        updatedArticleDto.setCoverUrl(updatedCoverUrl);
        updatedArticleDto.setContents(updatedContents);
        Article updateArticle = articleService.update(updatedArticleDto, user);

        // Then
        verify(articleRepository, atLeast(1)).findById(TEST_ID);
        assertThat(updateArticle.getTitle()).isEqualTo(updatedTitle);
        assertThat(updateArticle.getCoverUrl()).isEqualTo(updatedCoverUrl);
        assertThat(updateArticle.getContents()).isEqualTo(updatedContents);
    }

    @Test
    @DisplayName("article 삭제 테스트")
    void deleteArticle() {
        articleService.delete(TEST_ID);

        verify(articleRepository, atLeast(1)).deleteById(TEST_ID);
    }
}