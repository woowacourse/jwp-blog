package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.service.exception.AuthException;
import techcourse.myblog.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CommentService.class)
class CommentServiceTest {

    private static final long USER_ID = 1L;
    protected static final long ANOTHER_USER_ID = 2L;

    @Autowired
    CommentService commentService;

    @MockBean
    ArticleService articleService;

    @MockBean
    UserService userService;

    @MockBean
    CommentRepository commentRepository;

    private User user = new User(USER_ID, "email@gamil.com", "name", "P@ss0wrd");
    private Article article = new Article("title", "contents", "coverUrl", user);
    private Comment comment = new Comment("contents", user, article);
    private Comment anotherComment = new Comment("anotherComment", user, article);

    @Test
    void 댓글_저장() {
        // given
        CommentDto.Create commentCreate = mock(CommentDto.Create.class);
        when(userService.findById(USER_ID)).thenReturn(user);
        when(articleService.findById(any())).thenReturn(article);
        when(commentRepository.save(any())).thenReturn(comment);

        // when
        final CommentDto.Response commentResponse = commentService.save(commentCreate, USER_ID);

        // then
        verify(userService).findById(USER_ID);
        verify(articleService).findById(any());
        verify(commentRepository).save(any());
        assertThat(commentResponse.getContents()).isEqualTo(comment.getContents());
        assertThat(commentResponse.getName()).isEqualTo(user.getName());
    }

    @Test
    void 댓글_수정() {
        // given
        CommentDto.Update commentUpdate = mock(CommentDto.Update.class);
        when(commentUpdate.toComment()).thenReturn(anotherComment);
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        // when
        final CommentDto.Response commentResponse = commentService.update(commentUpdate, USER_ID);

        // then
        verify(commentRepository).findById(any());
        assertThat(commentResponse.getContents()).isEqualTo(anotherComment.getContents());
        assertThat(commentResponse.getName()).isEqualTo(user.getName());
    }

    @Test
    void 다른_작성자_댓글_수정_예외처리() {
        // given
        CommentDto.Update commentUpdate = mock(CommentDto.Update.class);
        when(commentUpdate.toComment()).thenReturn(anotherComment);
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        // when & then
        assertThrows(AuthException.class, () -> commentService.update(commentUpdate, ANOTHER_USER_ID));
    }

    @Test
    void 댓글_삭제() {
        // given
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        // when
        commentService.delete(1L, USER_ID);

        // then
        verify(commentRepository).delete(comment);
    }

    @Test
    void 다른_작성자_댓글_삭제_예외처리() {
        // given
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        // when & then
        assertThrows(AuthException.class, () -> commentService.delete(1L, ANOTHER_USER_ID));
    }
}