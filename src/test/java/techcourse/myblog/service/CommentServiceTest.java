package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.CommentDeleteException;
import techcourse.myblog.exception.CommentUpdateException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.CommentRequestDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    private static final Long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_EMAIL = "john123@example.com";
    private static final String DEFAULT_USER_NAME = "john";
    private static final String DEFAULT_USER_PASSWORD = "p@ssW0rd";

    private static final Long DEFAULT_ARTICLE_ID = 100L;
    private static final String DEFAULT_ARTICLE_TITLE = "some title";
    private static final String DEFAULT_ARTICLE_COVER_URL = "https://images.pexels.com/photos/731217/pexels-photo-731217.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940";
    private static final String DEFAULT_ARTICLE_CONTENTS = "## some cool one";

    private static final Long DEFAULT_COMMENT_ID = 200L;
    private static final String DEFAULT_COMMENT_CONTENTS = "some good comment";

    private User defaultUser;
    private Article defaultArticle;
    private Comment defaultComment;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setup() {
        defaultUser = spy(new User(DEFAULT_USER_EMAIL, DEFAULT_USER_NAME, DEFAULT_USER_PASSWORD));
        defaultArticle = spy(new Article(DEFAULT_ARTICLE_TITLE, DEFAULT_ARTICLE_COVER_URL, DEFAULT_ARTICLE_CONTENTS, defaultUser));
        defaultComment = spy(new Comment(DEFAULT_COMMENT_CONTENTS, defaultUser, defaultArticle));
    }

    @Test
    void 댓글_작성() {
        // Given
        CommentRequestDto commentDto = new CommentRequestDto(defaultComment.getContents());
        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(defaultUser));
        when(articleRepository.findById(DEFAULT_ARTICLE_ID)).thenReturn(Optional.of(defaultArticle));
        when(commentRepository.save(any())).thenReturn(defaultComment);

        // When
        commentService.save(commentDto, DEFAULT_USER_ID, DEFAULT_ARTICLE_ID);

        // Then
        verify(commentRepository).save(any());
    }

    @Test
    void 작성자가_댓글_수정() {
        // Given
        CommentRequestDto commentDto = new CommentRequestDto("new Hello");
        when(commentRepository.findById(DEFAULT_COMMENT_ID)).thenReturn(Optional.of(defaultComment));
        doReturn(true).when(defaultComment).matchAuthor(DEFAULT_USER_ID);

        // When
        commentService.update(commentDto, DEFAULT_COMMENT_ID, DEFAULT_USER_ID);

        // Then
        verify(defaultComment, atLeastOnce()).update(anyString());
    }

    @Test
    void 타인이_댓글_수정() {
        // Given
        CommentRequestDto commentDto = new CommentRequestDto("new Hello");
        when(defaultUser.getId()).thenReturn(DEFAULT_USER_ID);
        when(defaultComment.getId()).thenReturn(DEFAULT_COMMENT_ID);

        // When
        assertThrows(CommentUpdateException.class, () -> {
            commentService.update(commentDto, defaultComment.getId(), defaultComment.getAuthor().getId() + 1);
        });

        // Then
        verify(defaultComment, never()).update(anyString());
    }

    @Test
    void 작성자가_댓글_삭제() {
        // Given
        when(commentRepository.findById(DEFAULT_COMMENT_ID)).thenReturn(Optional.of(defaultComment));
        doReturn(true).when(defaultComment).matchAuthor(DEFAULT_USER_ID);

        // When
        commentService.delete(DEFAULT_COMMENT_ID, DEFAULT_USER_ID);

        // Then
        verify(commentRepository, atLeastOnce()).delete(any());
    }

    @Test
    void 타인이_댓글_삭제() {
        // Given
        when(defaultUser.getId()).thenReturn(DEFAULT_USER_ID);
        when(defaultComment.getId()).thenReturn(DEFAULT_COMMENT_ID);

        // When
        assertThrows(CommentDeleteException.class, () -> {
            commentService.delete(defaultComment.getId(), defaultComment.getAuthor().getId() + 1);
        });

        // Then
        verify(commentRepository, never()).delete(any());
    }
}
