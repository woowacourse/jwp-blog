package techcourse.myblog.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ArticleServiceTests {
    private static final Long USER_ID = 1L;
    private static final Long ARTICLE_ID = 1L;

    private static final String NAME = "bmo";
    private static final String EMAIL = "bmo@gmail.com";
    private static final long NOT_AUTHOR_USER_ID = 2L;

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private UserService userService;

    @Mock
    private ArticleRepository articleRepository;

    private User user = spy(new User("bmo", "Password123!", "bmo@gmail.com"));
    private User notAuthorUser = spy(new User("remo", "Password123!", "remo@reader.com"));
    private UserResponse userResponse = createUserResponse(USER_ID);
    private UserResponse notAuthorResponse = createUserResponse(NOT_AUTHOR_USER_ID);
    private Article article = spy(new Article(user, "title", "coverUrl", "contents"));
    private ArticleDto articleDto = new ArticleDto(USER_ID, "title", "coverUrl", "contents");


    @BeforeEach
    void setUp() {
        when(user.getId()).thenReturn(USER_ID);
        when(notAuthorUser.getId()).thenReturn(NOT_AUTHOR_USER_ID);
        doReturn(true).when(user).matchId(USER_ID);
        doReturn(true).when(notAuthorUser).matchId(NOT_AUTHOR_USER_ID);
    }

    @Test
    void 존재하지_않는_유저_게시글_생성_실패() {
        given(articleRepository.save(any())).willReturn(article);
        given(userService.findById(userResponse.getId())).willThrow(new NoUserException(""));
        assertThrows(NoUserException.class, () -> articleService.post(articleDto, userResponse.getId()));
    }

    @Test
    void 게시글_생성_성공() {
        given(userService.findById(USER_ID)).willReturn(user);
        given(articleRepository.save(any())).willReturn(article);
        given(user.getId()).willReturn(USER_ID);

        articleService.post(articleDto, userResponse.getId());

        verify(articleRepository).save(any());
    }

    @Test
    void 존재하지_않는_게시글_조회_실패() {
        assertThrows(NoArticleException.class, () -> articleService.findById(ARTICLE_ID));
    }

    @Test
    void 게시글_조회_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));

        articleService.findById(ARTICLE_ID);

        verify(articleRepository).findById(ARTICLE_ID);
    }

    @Test
    void 다른_사람이_작성한_게시글_조회_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        doReturn(false).when(article).matchAuthorId(NOT_AUTHOR_USER_ID);

        assertThrows(NotSameAuthorException.class, () ->
            articleService.findByAuthorId(ARTICLE_ID, notAuthorResponse.getId())
        );
    }

    @Test
    void 작성자가_작성한_게시글_조회_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));

        articleService.findByAuthorId(ARTICLE_ID, userResponse.getId());

        verify(articleRepository).findById(ARTICLE_ID);
    }

    @Test
    void 존재하지_않는_게시물_수정_실패() {
        assertThrows(NoArticleException.class, () ->
            articleService.update(articleDto, ARTICLE_ID, userResponse.getId()));
    }

    @Test
    void 다른_사람이_게시글_수정_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(NOT_AUTHOR_USER_ID)).willReturn(notAuthorUser);
        doReturn(false).when(article).matchAuthorId(NOT_AUTHOR_USER_ID);

        assertThrows(NotSameAuthorException.class, () ->
            articleService.update(articleDto, ARTICLE_ID, notAuthorResponse.getId()));
    }

    @Test
    void 게시물_수정_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(USER_ID)).willReturn(user);

        assertDoesNotThrow(() -> articleService.update(articleDto, ARTICLE_ID, userResponse.getId()));
    }

    @Test
    void 존재하지_않는_게시물_삭제_실패() {
        assertThrows(NoArticleException.class, () ->
            articleService.deleteById(ARTICLE_ID, userResponse.getId()));
    }

    @Test
    void 다른_사람이_게시글_삭제_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(NOT_AUTHOR_USER_ID)).willReturn(notAuthorUser);
        doReturn(false).when(article).matchAuthorId(NOT_AUTHOR_USER_ID);

        assertThrows(NotSameAuthorException.class, () ->
            articleService.deleteById(ARTICLE_ID, notAuthorResponse.getId()));
    }

    @Test
    void 게시물_삭제_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(USER_ID)).willReturn(user);

        assertDoesNotThrow(() -> articleService.deleteById(ARTICLE_ID, userResponse.getId()));
    }

    private UserResponse createUserResponse(Long userId) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setName(NAME);
        userResponse.setEmail(EMAIL);

        return userResponse;
    }
}
