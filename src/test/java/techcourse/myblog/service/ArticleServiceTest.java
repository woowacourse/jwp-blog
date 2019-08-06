package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.article.ArticleRequest;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.exception.article.ArticleAuthenticationException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {
    private static final String TITLE = "게시글 제목";
    private static final String CONTENTS = "게시글 내용";
    private static final String COVER_URL = "게시글 배경";

    private static final String NAME = "ike";
    private static final String PASSWORD = "@Password1234";
    private static final String EMAIL = "ike@ike.com";

    private static final String NAME_2 = "ikee";
    private static final String EMAIL_2 = "ike2@gmail.com";

    @Autowired
    private ArticleService articleService;

    @MockBean(name = "articleRepository")
    private ArticleRepository articleRepository;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    private Article article;
    private User author;
    private ArticleRequest articleRequestDto;
    private UserResponse userResponseDto;
    private UserResponse notAuthor;

    @BeforeEach
    void setUp() {
        author = new User(NAME, PASSWORD, EMAIL);
        article = new Article(TITLE, CONTENTS, COVER_URL, author);
        articleRequestDto = new ArticleRequest(TITLE, CONTENTS, COVER_URL);
        userResponseDto = new UserResponse(id, NAME, EMAIL);
        notAuthor = new UserResponse(id, NAME_2, EMAIL_2);
    }

    @Test
    public void 게시글_작성자와_수정_하려는_사용자가_일치하는_경우() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(userRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));

        // then
        assertDoesNotThrow(() -> articleService.update(1L, articleRequestDto, userResponseDto));
    }

    @Test
    public void 작성자_외의_사용자가_게시글을_수정하려고_하는_경우_예외처리() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(userRepository.findByEmail(notAuthor.getEmail())).thenReturn(Optional.of(author));

        // then
        assertThatThrownBy(() -> articleService.update(1L, articleRequestDto, notAuthor))
                .isInstanceOf(ArticleAuthenticationException.class);
    }

    @Test
    public void 게시글_작성자와_삭제_하려는_사용자가_일치하는_경우() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // then
        assertDoesNotThrow(() -> articleService.delete(1L, userResponseDto));
    }

    @Test
    public void 작성자_외의_사용자가_게시글을_삭제하려고_하는_경우_예외처리() {
        // given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // then
        assertThatThrownBy(() -> articleService.delete(1L, notAuthor))
                .isInstanceOf(ArticleAuthenticationException.class);
    }
}
