package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.CommentDeleteException;
import techcourse.myblog.exception.CommentUpdateException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.CommentRequestDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;

    private CommentService commentService;

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

        defaultComment = mock(Comment.class);
        when(defaultComment.getId()).thenReturn(DEFAULT_COMMENT_ID);
        when(defaultComment.getContents()).thenReturn(DEFAULT_COMMENT_CONTENTS);
        when(defaultComment.getAuthor()).thenReturn(defaultUser);
        when(defaultComment.getArticle()).thenReturn(defaultArticle);
        when(defaultComment.matchAuthor(DEFAULT_USER_ID)).thenReturn(true);

        userRepository = mock(UserRepository.class);
        when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
        when(userRepository.findById(AdditionalMatchers.not(eq(defaultUser.getId())))).thenThrow(UserNotFoundException.class);
        articleRepository = mock(ArticleRepository.class);
        when(articleRepository.findById(defaultArticle.getId())).thenReturn(Optional.of(defaultArticle));
        when(articleRepository.findById(AdditionalMatchers.not(eq(defaultArticle.getId())))).thenThrow(ArticleNotFoundException.class);
        commentRepository = mock(CommentRepository.class);
        when(commentRepository.save(any())).thenReturn(defaultComment);
        when(commentRepository.findById(defaultComment.getId())).thenReturn(Optional.of(defaultComment));

        commentService = new CommentService(commentRepository, userRepository, articleRepository);
    }

    @Test
    void 댓글_작성() {
        // Given
        CommentRequestDto commentDto = new CommentRequestDto(defaultComment.getContents());

        // When
        commentService.save(commentDto, defaultComment.getAuthor().getId(), defaultComment.getArticle().getId());

        // Then
        verify(commentRepository, atLeastOnce()).save(any());
    }

    @Test
    void 작성자가_댓글_수정() {
        // Given
        CommentRequestDto commentDto = new CommentRequestDto("new Hello");

        // When
        commentService.update(commentDto, defaultComment.getId(), defaultComment.getAuthor().getId());

        // Then
        verify(defaultComment, atLeastOnce()).update(anyString());
    }

    @Test
    void 타인이_댓글_수정() {
        // Given
        CommentRequestDto commentDto = new CommentRequestDto("new Hello");

        // When
        assertThrows(CommentUpdateException.class, () -> {
            commentService.update(commentDto, defaultComment.getId(), defaultComment.getAuthor().getId() + 1);
        });

        // Then
        verify(defaultComment, never()).update(anyString());
    }

    @Test
    void 작성자가_댓글_삭제() {
        // When
        commentService.delete(DEFAULT_COMMENT_ID, defaultComment.getAuthor().getId());

        // Then
        verify(commentRepository, atLeastOnce()).delete(any());
    }

    @Test
    void 타인이_댓글_삭제() {
        // When
        assertThrows(CommentDeleteException.class, () -> {
            commentService.delete(defaultComment.getId(), defaultComment.getAuthor().getId() + 1);
        });

        // Then
        verify(commentRepository, never()).delete(any());
    }
}
