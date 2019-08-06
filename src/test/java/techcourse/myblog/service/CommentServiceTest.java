package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleDetails;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotMatchAuthorException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    private final static Long DEFAULT_ID = 1L;
    private final static String USER_NAME = "name";
    private final static String USER_EMAIL = "aaa@gmail.com";
    private final static String USER_PASSWORD = "123QWE!@#";
    private final static String ARTICLE_TITLE = "title";
    private final static String ARTICLE_CONTENTS = "contents";
    private final static String ARTICLE_COVER_URL = "coverurl";
    private final static String COMMENT_CONTENTS = "comment";

    private Article article;
    private User user;
    private Comment comment;

    private UserDto.Response userDto;
    private CommentDto.Create commentCreateDto;
    private CommentDto.Update commentUpdateDto;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;


    @BeforeEach
    void setUp() {
        user = new User(DEFAULT_ID, USER_EMAIL, USER_PASSWORD, USER_NAME);
        article = new Article(DEFAULT_ID, new ArticleDetails(ARTICLE_TITLE, ARTICLE_COVER_URL, ARTICLE_CONTENTS), user);
        comment = new Comment(DEFAULT_ID, COMMENT_CONTENTS, user, article);
    }

    @Test
    void 댓글_작성() {
        // Given
        userDto = new UserDto.Response(DEFAULT_ID, USER_EMAIL, USER_NAME);
        commentCreateDto = new CommentDto.Create(DEFAULT_ID, COMMENT_CONTENTS);
        when(userRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(user));
        when(articleRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(article));
        when(commentRepository.save(comment)).thenReturn(comment);

        // When
        commentService.save(userDto, commentCreateDto);

        // Then
        verify(commentRepository).save(any());
    }


    @Test
    void 댓글_수정_테스트() {
        // Given
        userDto = new UserDto.Response(DEFAULT_ID, USER_EMAIL, USER_NAME);
        commentUpdateDto = new CommentDto.Update(DEFAULT_ID, COMMENT_CONTENTS + "a");
        when(commentRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(comment));

        // When
        commentService.update(userDto, DEFAULT_ID, commentUpdateDto);

        // Then
        verify(commentService).update(any(), any(), any());
    }

    @Test
    void 다른_작성자_댓글_수정_테스트() {
        // Given
        userDto = new UserDto.Response(DEFAULT_ID, USER_EMAIL, USER_NAME);
        commentUpdateDto = new CommentDto.Update(DEFAULT_ID, COMMENT_CONTENTS + "a");
        when(commentRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(comment));

        // When
        assertThrows(NotMatchAuthorException.class, () -> {
            commentService.update(userDto, DEFAULT_ID, commentUpdateDto);
        });


        // Then
        verify(commentService, never()).update(any(), any(), any());
    }

    @Test
    void 삭제_테스트() {
        // Given
        userDto = new UserDto.Response(DEFAULT_ID, USER_EMAIL, USER_NAME);
        when(commentRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(comment));

        // When
        commentService.deleteById(userDto, DEFAULT_ID);

        // Then
        verify(commentService).deleteById(any(), any());
    }

    @Test
    void 다른_작성자_삭제_실패_테스트() {
        // Given
        userDto = new UserDto.Response(DEFAULT_ID, USER_EMAIL, USER_NAME);
        when(commentRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(comment));

        // When
        assertThrows(NotMatchAuthorException.class, () -> {
            commentService.deleteById(userDto, DEFAULT_ID);
        });

        // Then
        verify(commentService).deleteById(any(), any());
    }
}