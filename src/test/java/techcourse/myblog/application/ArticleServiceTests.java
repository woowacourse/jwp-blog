package techcourse.myblog.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

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
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ModelMapper modelMapper;

    private User user = new User("bmo", "Password123!", "bmo@gmail.com");
    private User notAuthorUser = new User("remo", "Password123!", "remo@reader.com");
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
        assertThrows(NoUserException.class, () -> articleService.post(articleDto, userResponse));
    }

    @Test
    void 게시글_생성_성공() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.ofNullable(user));
        given(modelMapper.map(articleDto, Article.class)).willReturn(article);
        given(articleRepository.save(article)).willReturn(article);

        articleService.post(articleDto, userResponse);

        verify(articleRepository).save(article);
    }

    @Test
    void 존재하지_않는_게시글_조회_실패() {
        assertThrows(NoArticleException.class, () -> articleService.findArticleById(ARTICLE_ID));
    }

    @Test
    void 게시글_조회_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));

        articleService.findArticleById(ARTICLE_ID);

        verify(articleRepository).findById(ARTICLE_ID);
    }

    @Test
    void 다른_사람이_작성한_게시글_조회_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(modelMapper.map(notAuthorResponse, User.class)).willReturn(notAuthorUser);

        assertThrows(NotSameAuthorException.class, () ->
                articleService.findArticleWrittenByUser(ARTICLE_ID, notAuthorResponse)
        );
    }

    @Test
    void 작성자가_작성한_게시글_조회_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(modelMapper.map(userResponse, User.class)).willReturn(user);

        articleService.findArticleWrittenByUser(ARTICLE_ID, userResponse);

        verify(articleRepository).findById(ARTICLE_ID);
    }

    @Test
    void 존재하지_않는_게시물_수정_실패() {
        assertThrows(NoArticleException.class, () ->
                articleService.editArticle(articleDto, ARTICLE_ID, userResponse));
    }

    @Test
    void 존재하지_않는_유저_게시물_수정_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));

        assertThrows(NoUserException.class, () ->
                articleService.editArticle(articleDto, ARTICLE_ID, userResponse));
    }

    @Test
    void 다른_사람이_게시글_수정_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userRepository.findById(NOT_AUTHOR_USER_ID)).willReturn(Optional.ofNullable(notAuthorUser));

        assertThrows(NotSameAuthorException.class, () ->
                articleService.editArticle(articleDto, ARTICLE_ID, notAuthorResponse));
    }

    @Test
    void 게시물_수정_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userRepository.findById(USER_ID)).willReturn(Optional.ofNullable(user));
        given(modelMapper.map(articleDto, Article.class)).willReturn(article);

        assertDoesNotThrow(() -> articleService.editArticle(articleDto, ARTICLE_ID, userResponse));
    }

    @Test
    void 존재하지_않는_게시물_삭제_실패() {
        assertThrows(NoArticleException.class, () ->
                articleService.deleteById(ARTICLE_ID, userResponse));
    }

    @Test
    void 존재하지_않는_유저_게시물_삭제_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));

        assertThrows(NoUserException.class, () ->
                articleService.deleteById(ARTICLE_ID, userResponse));
    }

    @Test
    void 다른_사람이_게시글_삭제_실패() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userRepository.findById(NOT_AUTHOR_USER_ID)).willReturn(Optional.ofNullable(notAuthorUser));

        assertThrows(NotSameAuthorException.class, () ->
                articleService.deleteById(ARTICLE_ID, notAuthorResponse));
    }

    @Test
    void 게시물_삭제_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userRepository.findById(USER_ID)).willReturn(Optional.ofNullable(user));

        assertDoesNotThrow(() -> articleService.deleteById(ARTICLE_ID, userResponse));
    }

    private UserResponse createUserResponse(Long userId) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setName(NAME);
        userResponse.setEmail(EMAIL);

        return userResponse;
    }
}
