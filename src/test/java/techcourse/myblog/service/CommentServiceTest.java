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

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    private Comment comment;
    private Article article;
    private User author;

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

        CommentSaveRequestDto commentSaveRequestDto = new CommentSaveRequestDto();
        commentSaveRequestDto.setArticleId(articleId);
        commentSaveRequestDto.setComment("댓글");
        comment = commentService.save(commentSaveRequestDto, author);
    }

    @Test
    void findByArticleId() {
        List<Comment> comments = commentService.findByArticleId(article.getId());

        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    @Test
    void update() {
        String editedContents = "수정된내용";
        commentService.update(comment.getId(), editedContents, author);

        Comment editedComment = commentService.findById(this.comment.getId());
        assertThat(editedComment.getContents()).isEqualTo(editedContents);
    }

    @Test
    void findArticleIdById() {
        Long articleId = commentService.findArticleIdById(comment.getId());

        assertThat(articleId).isEqualTo(article.getId());
    }

    @Test
    void delete() {
        commentService.deleteById(comment.getId(), author);

        assertThrows(CommentNotFoundException.class, () -> commentService.findById(comment.getId()));
    }
}