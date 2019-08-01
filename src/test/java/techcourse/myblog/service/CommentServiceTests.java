package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.CommentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class CommentServiceTests {
    @Mock
    protected CommentRepository commentRepository;
    protected CommentService commentService;
    private User user;
    private Article article;
    private Long id = 2L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentService(commentRepository);
        user = new User("name", "email@email.com", "Passw0rd!");
        article = new Article("title", "coverUrl", "contents", user);
    }

    @Test
    void 댓글_수정() {
        Comment comment = new Comment("comment", user, article);
        Comment updateComment = new Comment("new comment", user, article);
        given(commentRepository.findById(id)).willReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.modify(id, updateComment));
        compareComment(commentRepository.findById(id).get(), updateComment);
    }

    void compareComment(Comment comment1, Comment comment2) {
        assertEquals(comment1.getContents(), comment2.getContents());
        assertEquals(comment1.getArticle(), comment2.getArticle());
        assertEquals(comment1.getWriter(), comment2.getWriter());
    }
}
