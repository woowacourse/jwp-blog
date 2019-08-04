package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.ArticleDeleteException;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.ArticleRequestDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ArticleServiceTest {
    private static final Long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_EMAIL = "john123@example.com";
    private static final String DEFAULT_USER_NAME = "john";
    private static final String DEFAULT_USER_PASSWORD = "p@ssW0rd";

    private static final Long DEFAULT_ARTICLE_ID = 100L;
    private static final String DEFAULT_ARTICLE_TITLE = "some title";
    private static final String DEFAULT_ARTICLE_COVER_URL = "https://images.pexels.com/photos/731217/pexels-photo-731217.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940";
    private static final String DEFAULT_ARTICLE_CONTENTS = "## some cool one";

    private User defaultUser;
    private Article defaultArticle;

    private UserRepository userRepository;
    private ArticleRepository articleRepository;

    private ArticleService articleService;

    @BeforeEach
    void setup() {
        defaultUser = mock(User.class);
        when(defaultUser.getId()).thenReturn(DEFAULT_USER_ID);
        when(defaultUser.getEmail()).thenReturn(DEFAULT_USER_EMAIL);
        when(defaultUser.getName()).thenReturn(DEFAULT_USER_NAME);
        when(defaultUser.getPassword()).thenReturn(DEFAULT_USER_PASSWORD);

        defaultArticle = mock(Article.class);
        when(defaultArticle.getId()).thenReturn(DEFAULT_ARTICLE_ID);
        when(defaultArticle.getTitle()).thenReturn(DEFAULT_ARTICLE_TITLE);
        when(defaultArticle.getCoverUrl()).thenReturn(DEFAULT_ARTICLE_COVER_URL);
        when(defaultArticle.getContents()).thenReturn(DEFAULT_ARTICLE_CONTENTS);
        when(defaultArticle.getAuthor()).thenReturn(defaultUser);
        when(defaultArticle.matchAuthor(any())).thenReturn(false);
        when(defaultArticle.matchAuthor(eq(DEFAULT_USER_ID))).thenReturn(true);

        articleRepository = mock(ArticleRepository.class);
        when(articleRepository.save(any())).thenReturn(defaultArticle);
        when(articleRepository.findById(defaultArticle.getId())).thenReturn(Optional.of(defaultArticle));
        when(articleRepository.findById(AdditionalMatchers.not(eq(defaultArticle.getId())))).thenThrow(ArticleNotFoundException.class);
        when(articleRepository.findAll()).thenReturn(Collections.singletonList(defaultArticle));
        userRepository = mock(UserRepository.class);
        when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
        when(userRepository.findById(AdditionalMatchers.not(eq(defaultUser.getId())))).thenThrow(UserNotFoundException.class);

        articleService = new ArticleService(articleRepository, userRepository);
    }

    @Test
    void 게시글_생성_확인() {
        // Given
        ArticleRequestDto article = new ArticleRequestDto(DEFAULT_ARTICLE_TITLE, DEFAULT_ARTICLE_COVER_URL, DEFAULT_ARTICLE_CONTENTS);

        // When
        Long articleId = articleService.save(article, DEFAULT_USER_ID);

        // Then
        assertThat(articleId).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(articleService.findById(articleId)).isEqualTo(defaultArticle);
        verify(articleRepository).save(any());
    }


    @Test
    void 게시글_조회_확인() {
        // When
        Article retrieveArticleDto = articleService.findById(DEFAULT_ARTICLE_ID);

        // Then
        assertThat(retrieveArticleDto).isEqualTo(articleService.findById(DEFAULT_ARTICLE_ID));
    }

    @Test
    void 모든_게시글_조회_확인() {
        // When
        List<Article> articleDtos = articleService.findAll();

        // Then
        assertThat(articleDtos)
            .hasSize(1)
            .containsExactly(articleService.findById(DEFAULT_ARTICLE_ID));
    }

    @Test
    void 작성자가_게시글_수정() {
        // Given
        ArticleRequestDto updateRequestDto = new ArticleRequestDto("title", "", "contents");

        // When
        articleService.update(DEFAULT_ARTICLE_ID, updateRequestDto, DEFAULT_USER_ID);

        // Then
        verify(defaultArticle).update(any());
    }

    @Test
    void 타인이_게시글_수정() {
        // Given
        ArticleRequestDto updateRequestDto = new ArticleRequestDto("title", "", "contents");

        // When
        articleService.update(DEFAULT_ARTICLE_ID, updateRequestDto, DEFAULT_ARTICLE_ID + 1);

        // Then
        verify(defaultArticle, never()).update(any());
    }

    @Test
    void 작성자가_게시글_삭제() {
        // When
        articleService.delete(DEFAULT_ARTICLE_ID, DEFAULT_USER_ID);

        // Then
        verify(articleRepository).delete(any());
    }

    @Test
    void 타인이_게시글_삭제() {
        // When
        assertThrows(ArticleDeleteException.class, () -> {
            articleService.delete(DEFAULT_ARTICLE_ID, DEFAULT_USER_ID + 1);
        });

        // Then
        verify(articleRepository, never()).delete(any());
    }
}
