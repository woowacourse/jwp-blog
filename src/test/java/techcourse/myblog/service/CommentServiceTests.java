package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.CommentUpdateFailedException;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.dto.CommentDto;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class CommentServiceTests {
    @Mock
    private CommentRepository commentRepository;
    private CommentService commentService;

    private User author = new User("name", "email@email.com", "Passw0rd!");
    private Article article = new Article("title", "coverUrl", "contents", author);
    private Comment comment = new Comment("comment", author, article);
    private Long id = 2L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentService(commentRepository);
        given(commentRepository.findById(id)).willReturn(Optional.of(comment));
    }

    @Test
    void 댓글_수정_성공() {
        CommentDto updateCommentDto = new CommentDto("new comment");

        assertDoesNotThrow(() -> commentService.modify(id, updateCommentDto, author));
        assertThat(commentRepository.findById(id).get().getContents())
                .isEqualTo(updateCommentDto.getContents());
    }

    @Test
    void 댓글_수정_실패() {
        User notAuthor = new User("notAuthor", "not@mail.com", "Passw0rd!");
        CommentDto updateCommentDto = new CommentDto("new comment");
        given(commentRepository.findById(id)).willReturn(Optional.of(comment));

        assertThrows(CommentUpdateFailedException.class, () ->
                commentService.modify(id, updateCommentDto, notAuthor));
    }
}
