package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleSaveRequestDto;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.exception.IllegalCommentDeleteRequestException;
import techcourse.myblog.exception.IllegalCommentUpdateRequestException;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    private User author;
    private Article article;
    private CommentSaveRequestDto commentSaveRequestDto;
    private Comment comment;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .name("이름")
                .email("test123@test.com")
                .password("asdfas1!")
                .build();
        userService.save(author);

        ArticleSaveRequestDto articleSaveRequestDto = new ArticleSaveRequestDto("title", "coverUrl", "contents");
        article = articleService.save(articleSaveRequestDto, author);

        Long articleId = article.getId();

        commentSaveRequestDto = new CommentSaveRequestDto();
        commentSaveRequestDto.setArticleId(articleId);
        commentSaveRequestDto.setContents("댓글");
        comment = commentService.save(commentSaveRequestDto, author);
    }

    @Test
    void findByArticleId() {
        List<Comment> comments = commentService.findByArticleId(article.getId());

        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    @Test
    void findArticleIdById() {
        Long articleId = commentService.findArticleIdById(comment.getId());

        assertThat(articleId).isEqualTo(article.getId());
    }

    @Test
    void update() {
        String editedContents = "수정된내용";
        commentService.update(comment.getId(), editedContents, author);

        Comment editedComment = commentService.findById(this.comment.getId());
        assertThat(editedComment.getContents()).isEqualTo(editedContents);
    }

    @Test
    void update_작성자가_아닌_경우() {
        User anotherAuthor = User.builder()
                .name("이름")
                .email("anotherAuthor@test.com")
                .password("password1!")
                .build();
        userService.save(anotherAuthor);

        String editedContents = "수정된내용";

        assertThrows(IllegalCommentUpdateRequestException.class
                , () -> commentService.update(comment.getId(), editedContents, anotherAuthor));

        Comment updatedComment = commentService.findById(comment.getId());
        assertThat(updatedComment.getContents()).isEqualTo(commentSaveRequestDto.getContents());
    }

    @Test
    void delete() {
        commentService.deleteById(comment.getId(), author);

        assertThrows(CommentNotFoundException.class, () -> commentService.findById(comment.getId()));
    }

    @Test
    void delete_작성자가_아닌_경우() {
        User anotherAuthor = User.builder()
                .name("이름")
                .email("anotherAuthor@test.com")
                .password("password1!")
                .build();
        userService.save(anotherAuthor);

        assertThrows(IllegalCommentDeleteRequestException.class
                , () -> commentService.deleteById(comment.getId(), anotherAuthor));

        assertDoesNotThrow(() -> commentService.findById(comment.getId()));
    }
}