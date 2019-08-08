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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        Comment updateComment = new Comment("new comment", author, article);

        assertDoesNotThrow(() -> commentService.modify(id, updateComment));
        compareComment(commentRepository.findById(id).get(), updateComment);
    }

    @Test
    void 댓글_수정_실패() {
        User notAuthor = new User("notAuthor", "not@mail.com", "Passw0rd!");
        Comment updateComment = new Comment("new comment", notAuthor, article);
        given(commentRepository.findById(id)).willReturn(Optional.of(comment));

        assertThrows(CommentUpdateFailedException.class, () ->
                commentService.modify(id, updateComment));
    }

    void compareComment(Comment comment1, Comment comment2) {
        assertEquals(comment1.getContents(), comment2.getContents());
        assertEquals(comment1.getArticle(), comment2.getArticle());
        assertEquals(comment1.getWriter(), comment2.getWriter());
    }
}
