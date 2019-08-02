package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleRequest;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.exception.InvalidAuthorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Test
    public void 게시글을_잘_저장하는지_확인한다() {
        User user = userService.findUserByEmail("cony@cony.com");
        ArticleRequest articleDto = new ArticleRequest("제목", "커버", "내용");

        assertDoesNotThrow(() -> articleService.save(articleDto, user));
    }

    @Test
    public void 게시글을_잘_수정하는지_확인한다() {
        User user = userService.findUserByEmail("cony@cony.com");
        ArticleRequest articleDto = new ArticleRequest("제목", "커버", "수정된 내용");
        Article updatedArticle = articleService.update(1, articleDto, user);

        assertThat(updatedArticle.getContents()).isEqualTo(articleDto.getContents());
    }

    @Test
    public void 작성자가_아닌_사람이_게시글을_수정하려_하면_예외를_던진다() {
        User user = userService.findUserByEmail("buddy@buddy.com");
        ArticleRequest articleDto = new ArticleRequest("제목", "커버", "수정된 내용");

        assertThrows(InvalidAuthorException.class, () -> {
            articleService.update(1, articleDto, user);
        });
    }

    @Test
    public void 게시글을_잘_삭제하는지_확인한다() {
        User user = userService.findUserByEmail("cony@cony.com");

        assertDoesNotThrow(() -> articleService.deleteById(2, user));
    }

    @Test
    public void 작성자가_아닌_사람이_게시글을_삭제하려_하면_예외를_던진다() {
        User user = userService.findUserByEmail("buddy@buddy.com");

        assertThrows(InvalidAuthorException.class, () -> {
            articleService.deleteById(1, user);
        });
    }

    @Test
    public void 댓글을_잘_저장하는지_확인한다() {
        User user = userService.findUserByEmail("cony@cony.com");
        CommentRequest commentDto = new CommentRequest("댓글입니다");

        assertDoesNotThrow(() -> articleService.saveComment(1, commentDto, user));
    }

    @Test
    public void 댓글을_잘_수정하는지_확인한다() {
        User user = userService.findUserByEmail("cony@cony.com");
        CommentRequest commentDto = new CommentRequest("수정한 댓글입니다");
        Comment updatedComment = articleService.updateComment(1, commentDto, user);

        assertThat(updatedComment.getContents()).isEqualTo(commentDto.getContents());
    }

    @Test
    public void 작성자가_아닌_사람이_댓글을_수정하려_하면_예외를_던진다() {
        User user = userService.findUserByEmail("buddy@buddy.com");
        CommentRequest commentDto = new CommentRequest("수정한 댓글입니다");

        assertThrows(InvalidAuthorException.class, () -> {
            articleService.updateComment(1, commentDto, user);
        });
    }

    @Test
    public void 댓글을_잘_삭제하는지_확인한다() {
        User user = userService.findUserByEmail("cony@cony.com");

        assertDoesNotThrow(() -> articleService.deleteComment(2, user));
    }

    @Test
    public void 작성자가_아닌_사람이_댓글을_삭제하려_하면_예외를_던진다() {
        User user = userService.findUserByEmail("buddy@buddy.com");

        assertThrows(InvalidAuthorException.class, () -> {
            articleService.deleteComment(1, user);
        });
    }
}
