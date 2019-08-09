package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.application.service.exception.NotMatchAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.vo.CommentContents;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTests {
    private CommentService commentService;

    @MockBean(name = "commentRepository")
    private CommentRepository commentRepository;
    @MockBean(name = "articleService")
    private ArticleService articleService;

    @Autowired
    public CommentServiceTests(CommentService commentService) {
        this.commentService = commentService;
    }

    @Test
    void modify_comment_test() {
        CommentContents firstCommentContents = new CommentContents("ehem");
        CommentContents secondCommentContents = new CommentContents("ehemehem");
        User author = new User("easy@gmail.com", "easy", "qwerasdf");
        Article article = new Article.ArticleBuilder()
                .author(author).title("111").contents("2222").coverUrl("333").build();
        Comment firstComment = new Comment(firstCommentContents, author, article);

        Long id = Long.valueOf("100");
        when(commentRepository.findById(id)).thenReturn(Optional.of(firstComment));

        commentService.modify(id, secondCommentContents);
        assertThat(firstComment.getContents().equals("ehemehem"));
    }

    @Test
    void check_author_test() {
        CommentContents firstCommentContents = new CommentContents("ehem");
        User author = new User("easy@gmail.com", "easy", "qwerasdf");
        Article article = new Article.ArticleBuilder()
                .author(author).title("111").contents("2222").coverUrl("333").build();

        Comment comment = new Comment(firstCommentContents, author, article);
        Long id = Long.valueOf("100");

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        assertThrows(NotMatchAuthorException.class, () -> commentService.checkAuthor(id, "hello@gmail.com"));
    }
}
