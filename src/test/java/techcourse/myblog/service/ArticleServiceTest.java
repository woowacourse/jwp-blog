package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.AuthException;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    private static final long ARTICLE_ID = 1L;
    private static final Long USER_ID = 1L;

    @InjectMocks
    ArticleService articleService;

    @Mock
    UserService userService;

    @Mock
    ArticleRepository articleRepository;

    private User user = new User(USER_ID, "email@gamil.com", "name", "P@ssw0rd");
    private ArticleDto.Request articleRequest = new ArticleDto.Request(ARTICLE_ID, "contents", "title", "coverUrl");
    private Article article = articleRequest.toArticle(user);

    @Test
    void 게시글_저장() {
        // given
        when(userService.findById(USER_ID)).thenReturn(user);
        when(articleRepository.save(article)).thenReturn(article);

        // when
        articleService.save(USER_ID, articleRequest);

        // then
        verify(articleRepository).save(article);
    }

    @Test
    void 존재하는_게시글_조회() {
        // given
        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.ofNullable(article));

        // when
        articleService.findById(ARTICLE_ID);

        // then
        verify(articleRepository).findById(ARTICLE_ID);
    }

    @Test
    void 존재하지_않는_게시글_조회_예외처리() {
        // given
        final long articleId = 100L;
        when(articleRepository.findById(articleId)).thenThrow(IllegalArgumentException.class);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> articleService.findById(articleId));
        verify(articleRepository).findById(articleId);
    }

    @Test
    void 게시글_수정() {
        // given
        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.ofNullable(article));

        // when & then
        assertDoesNotThrow(() -> articleService.edit(USER_ID, articleRequest));
        verify(articleRepository).findById(ARTICLE_ID);
    }

    @Test
    void 다른_작성자_게시글_수정_예외처리() {
        // given
        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.ofNullable(article));

        // when & then
        assertThrows(AuthException.class, () -> articleService.edit(2L, articleRequest));
    }

    @Test
    void 게시글_삭제() {
        // given
        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.ofNullable(article));

        // when
        articleService.deleteById(USER_ID, ARTICLE_ID);

        // then
        verify(articleRepository).deleteById(ARTICLE_ID);
    }

    @Test
    void 다른_작성자_게시글_삭제_예외처리() {
        // given
        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.ofNullable(article));

        // when & then
        assertThrows(AuthException.class, () -> articleService.deleteById(10L, ARTICLE_ID));
        verify(articleRepository, never()).deleteById(ARTICLE_ID);
    }

    @Test
    void Article_페이지_조회() {
        // given
        Pageable pageable = mock(Pageable.class);
        Page articles = mock(Page.class);
        when(articleRepository.findAll(pageable)).thenReturn(articles);

        // when
        Page<Article> expected = articleService.findAll(pageable);

        // then
        verify(articleRepository).findAll(pageable);
        assertThat(articles).isEqualTo(expected);
    }

    @Test
    void 특정_author_Article_전체_조회() {
        // given
        List articles = mock(List.class);
        when(articleRepository.findAllByAuthor(user)).thenReturn(articles);
        when(userService.findByEmail(user.getEmail())).thenReturn(user);

        // when
        List<Article> expected = articleService.findAllByAuthor(user.getEmail());

        // then
        verify(articleRepository, never()).findAll();
        verify(articleRepository).findAllByAuthor(user);
        assertThat(articles).isEqualTo(expected);
    }

    @Test
    void Article_전체_조회() {
        // given
        List articles = mock(List.class);
        when(articleRepository.findAll()).thenReturn(articles);

        // when
        List<Article> expected = articleService.findAllByAuthor(null);

        // then
        verify(articleRepository).findAll();
        verify(articleRepository, never()).findAllByAuthor(null);
        assertThat(articles).isEqualTo(expected);
    }
}