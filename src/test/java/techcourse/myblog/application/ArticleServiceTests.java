package techcourse.myblog.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.assembler.ArticleAssembler;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

    @Mock
    private ArticleAssembler articleAssembler;

    private User user = new User("bmo", "bmo@gmail.com", "Password123!");
    private User notAuthorUser = new User("remo", "remo@reader.com", "Password123!");
    private UserResponse userResponse = createUserResponse(USER_ID);
    private UserResponse notAuthorResponse = createUserResponse(NOT_AUTHOR_USER_ID);
    private Article article = new Article(user, "title", "coverUrl", "contents");
    private ArticleDto articleDto = new ArticleDto(USER_ID, "title", "coverUrl", "contents");


    @BeforeEach
    void setUp() {
        user.setId(USER_ID);
        notAuthorUser.setId(NOT_AUTHOR_USER_ID);
    }

    @Test
    void 존재하지_않는_유저_게시글_생성_실패() {
        given(userService.findById(USER_ID)).willThrow(NoUserException.class);
        assertThrows(NoUserException.class, () -> articleService.save(articleDto, userResponse));
    }

    @Test
    void 게시글_생성_성공() {
        given(userService.findById(USER_ID)).willReturn(user);
        given(articleAssembler.convertToArticle(articleDto, user)).willReturn(article);
        given(articleRepository.save(article)).willReturn(article);

        articleService.save(articleDto, userResponse);

        verify(articleRepository).save(article);
    }

    @Test
    void 존재하지_않는_게시글_조회_실패() {
        assertThrows(NoArticleException.class,
                () -> articleService.findById(ARTICLE_ID));
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
        given(userService.findById(NOT_AUTHOR_USER_ID)).willReturn(notAuthorUser);

        assertThrows(NotSameAuthorException.class, () ->
                articleService.find(ARTICLE_ID, notAuthorResponse)
        );
    }

    @Test
    void 작성자가_작성한_게시글_조회_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(USER_ID)).willReturn(user);

        articleService.find(ARTICLE_ID, userResponse);

        verify(articleRepository).findById(ARTICLE_ID);
    }

    @Test
    void 존재하지_않는_게시물_수정_실패() {
        assertThrows(NoArticleException.class, () ->
                articleService.modify(articleDto, ARTICLE_ID, userResponse));
    }

    @Test
    void 존재하지_않는_유저_게시물_수정_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(USER_ID)).willThrow(NoUserException.class);

        assertThrows(NoUserException.class, () ->
                articleService.modify(articleDto, ARTICLE_ID, userResponse));
    }

    @Test
    void 다른_사람이_게시글_수정_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(NOT_AUTHOR_USER_ID)).willReturn(notAuthorUser);

        assertThrows(NotSameAuthorException.class, () ->
                articleService.modify(articleDto, ARTICLE_ID, notAuthorResponse));
    }

    @Test
    void 게시물_수정_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(USER_ID)).willReturn(user);
        given(articleAssembler.convertToArticle(articleDto, user)).willReturn(article);

        assertDoesNotThrow(() -> articleService.modify(articleDto, ARTICLE_ID, userResponse));
    }

    @Test
    void 존재하지_않는_게시물_삭제_실패() {
        assertThrows(NoArticleException.class, () ->
                articleService.remove(ARTICLE_ID, userResponse));
    }

    @Test
    void 존재하지_않는_유저_게시물_삭제_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(USER_ID)).willThrow(NoUserException.class);

        assertThrows(NoUserException.class, () ->
                articleService.remove(ARTICLE_ID, userResponse));
    }

    @Test
    void 다른_사람이_게시글_삭제_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(NOT_AUTHOR_USER_ID)).willReturn(notAuthorUser);

        assertThrows(NotSameAuthorException.class, () ->
                articleService.remove(ARTICLE_ID, notAuthorResponse));
    }

    @Test
    void 게시물_삭제_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userService.findById(USER_ID)).willReturn(user);

        assertDoesNotThrow(() -> articleService.remove(ARTICLE_ID, userResponse));
    }

    private UserResponse createUserResponse(Long userId) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setName(NAME);
        userResponse.setEmail(EMAIL);

        return userResponse;
    }
}