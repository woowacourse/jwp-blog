package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.application.converter.CommentConverter;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.NotMatchAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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
        User author = new User("easy@gmail.com", "easy", "qwerasdf");
        Article article = new Article.ArticleBuilder()
                .author(author).title("111").contents("2222").coverUrl("333").build();

        CommentDto originalCommnetDto = new CommentDto(Long.valueOf("100"), "ehem", author, article);
        Comment comment = CommentConverter.getInstance().convertFromDto(originalCommnetDto);

        CommentDto newCommentDto = new CommentDto(Long.valueOf("100"), "ehemehem", author, article);

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        commentService.modify(comment.getId(), newCommentDto);
        assertThat(comment.getContents().equals("ehemehem"));
    }

    @Test
    void check_author_test() {
        User author = new User("easy@gmail.com", "easy", "qwerasdf");
        Article article = new Article.ArticleBuilder()
                .author(author).title("111").contents("2222").coverUrl("333").build();

        CommentDto originalCommnetDto = new CommentDto(Long.valueOf("100"), "ehem", author, article);
        Comment comment = CommentConverter.getInstance().convertFromDto(originalCommnetDto);

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        assertThrows(NotMatchAuthorException.class, () -> commentService.checkAuthor(comment.getId(), "hello@gmail.com"));
    }

    @Test
    void findAllCommentsByArticleId_test() {
        User author = new User("easy@gmail.com", "easy", "qwerasdf");
        Long articleId = Long.valueOf("11");

        Article article = new Article.ArticleBuilder()
                .author(author).title("111").contents("2222").coverUrl("333").build();

        CommentDto firstCommentDto = new CommentDto(Long.valueOf("100"), "ehem", author, article);
        Comment firstComment = CommentConverter.getInstance().convertFromDto(firstCommentDto);
        CommentDto secondCommentDto = new CommentDto(Long.valueOf("101"),"ehemehem", author, article);
        Comment secondComment = CommentConverter.getInstance().convertFromDto(secondCommentDto);

        List<Comment> mockList = Arrays.asList(firstComment, secondComment);

        when(commentRepository.findByArticle(article)).thenReturn(mockList);
        when(articleService.findById(articleId)).thenReturn(article);
        List<CommentDto> checkList = commentService.findAllCommentsByArticleId(articleId, "easy@gmail.com");

        checkList.equals(mockList);
    }

}
