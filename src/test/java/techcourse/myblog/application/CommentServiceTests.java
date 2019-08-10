package techcourse.myblog.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.CommentNotFoundException;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.domain.repository.UserRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CommentServiceTests {
    private static final Long USER_ID = 1L;
    private static final Long ARTICLE_ID = 1L;
    private static final Long COMMENT_ID = 1L;

    private static final String NAME = "bmo";
    private static final String EMAIL = "bmo@gmail.com";
    private static final long NOT_AUTHOR_USER_ID = 2L;


    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ModelMapper modelMapper;

    private User user = new User("bmo", "bmo@gmail.com", "Password123!");
    private User notAuthorUser = new User("remo", "remo@reader.com", "Password123!");
    private Article article = new Article(user, "title", "coverUrl", "contents");
    private Comment comment = new Comment("commentContents", user, article);
    private CommentRequest commentRequest = new CommentRequest("commentContents");

    @Test
    void 댓글작성_성공() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));
        given(userRepository.findById(USER_ID)).willReturn(Optional.ofNullable(user));

        commentService.save(commentRequest, USER_ID, ARTICLE_ID);

        verify(commentRepository).save(comment);
    }

    @Test
    void 댓글작성_존재하지_않는_게시글_오류() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.ofNullable(user));

        assertThrows(NoArticleException.class,
                () -> commentService.save(commentRequest, USER_ID, ARTICLE_ID));
    }

    @Test
    void 댓글작성_존재하지_않는_유저_오류() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.ofNullable(article));

        assertThrows(NoUserException.class,
                () -> commentService.save(commentRequest, USER_ID, ARTICLE_ID));
    }

    @Test
    void 해당_게시글의_전체_댓글조회_성공() {
        given(commentRepository.findAllByArticle(article)).willReturn(Arrays.asList(comment));
        commentService.findAllByArticle(article);
        verify(commentRepository).findAllByArticle(article);
    }

    @Test
    void 댓글조회_성공() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.of(comment));
        commentService.findById(COMMENT_ID);
        verify(commentRepository).findById(COMMENT_ID);
    }

    @Test
    void 댓글조회_실패() {
        assertThrows(CommentNotFoundException.class,
                () -> commentService.findById(COMMENT_ID));
    }

    @Test
    void 댓글삭제_성공() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.ofNullable(comment));
        given(userRepository.findById(USER_ID)).willReturn(Optional.ofNullable(user));

        UserResponse userResponse = 사용자_응답_만들기(USER_ID);

        commentService.remove(COMMENT_ID, userResponse);
        verify(commentRepository).deleteById(COMMENT_ID);
    }

    @Test
    void 존재하지_않는_회원_댓글삭제_실패() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.ofNullable(comment));

        UserResponse userResponse = 사용자_응답_만들기(NOT_AUTHOR_USER_ID);

        assertThrows(NoUserException.class,
                () -> commentService.remove(COMMENT_ID, userResponse));
    }

    @Test
    void 작성자가_아닌_회원이_댓글_삭제_실패() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.ofNullable(comment));
        notAuthorUser.setId(NOT_AUTHOR_USER_ID);
        given(userRepository.findById(NOT_AUTHOR_USER_ID)).willReturn(Optional.ofNullable(notAuthorUser));

        UserResponse userResponse = 사용자_응답_만들기(NOT_AUTHOR_USER_ID);

        assertThrows(NotSameAuthorException.class,
                () -> commentService.remove(COMMENT_ID, userResponse));
    }

    @Test
    void 댓글수정_성공() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.ofNullable(comment));
        given(userRepository.findById(USER_ID)).willReturn(Optional.ofNullable(user));
        given(modelMapper.map(commentRequest, Comment.class)).willReturn(comment);

        UserResponse userResponse = 사용자_응답_만들기(USER_ID);

        assertDoesNotThrow(() -> commentService.modify(COMMENT_ID, userResponse, commentRequest));
    }

    @Test
    void 존재하지_않는_회원_댓글수정_실패() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.ofNullable(comment));

        UserResponse userResponse = 사용자_응답_만들기(NOT_AUTHOR_USER_ID);

        assertThrows(NoUserException.class,
                () -> commentService.modify(COMMENT_ID, userResponse, commentRequest));
    }

    @Test
    void 작성자가_아닌_회원이_댓글_수정_실패() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.ofNullable(comment));
        notAuthorUser.setId(NOT_AUTHOR_USER_ID);
        given(userRepository.findById(NOT_AUTHOR_USER_ID)).willReturn(Optional.ofNullable(notAuthorUser));
        given(modelMapper.map(commentRequest, Comment.class)).willReturn(comment);

        UserResponse userResponse = 사용자_응답_만들기(NOT_AUTHOR_USER_ID);

        assertThrows(NotSameAuthorException.class,
                () -> commentService.modify(COMMENT_ID, userResponse, commentRequest));
    }

    private UserResponse 사용자_응답_만들기(Long 아이디) {
        UserResponse 사용자_응답 = new UserResponse();
        사용자_응답.setId(아이디);
        사용자_응답.setName(NAME);
        사용자_응답.setEmail(EMAIL);

        return 사용자_응답;
    }
}