package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.exception.NoPermissionCommentException;
import techcourse.myblog.service.exception.NoRowException;
import techcourse.myblog.web.dto.ArticleDto;
import techcourse.myblog.web.dto.CommentDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentServiceTest {
    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;

    private User author;
    private Article article;
    private Comment beforeComment;

    @Autowired
    public CommentServiceTest(UserService userService, ArticleService articleService, CommentService commentService) {
        this.userService = userService;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @BeforeEach
    void setUp() {
        author = userService.findByEmail("aiden@woowa.com");
        article = articleService.save(new ArticleDto("gogo", "coverUrl", "contents"), author);
        CommentDto commentDto = new CommentDto("brand new comment");
        beforeComment = commentService.save(commentDto, author, article);
    }

    @Test
    @Transactional
    void 저장_조회_테스트() {
        Comment comment = commentService.save(new CommentDto("contents"), author, article);
        assertThat(commentService.findById(comment.getId())).isEqualTo(comment);
    }

    @Test
    void 수정_테스트() {
        CommentDto newCommentDto = new CommentDto("update");
        Comment newComment = commentService.update(beforeComment.getId(), newCommentDto, author);
        assertThat(newComment.getContents()).isEqualTo("update");
    }

    @Test
    void 코멘트_목록_테스트() {
        CommentDto commentDto = new CommentDto("brand new comment");
        commentService.save(commentDto, author, article);
        assertThat(commentService.findByArticle(article).size()).isEqualTo(2);
    }

    @Test
    void 삭제_테스트() {
        Long beforeCommentId = beforeComment.getId();
        commentService.delete(beforeCommentId, author);
        assertThatThrownBy(() -> {
            commentService.findById(beforeCommentId);
        }).isInstanceOf(NoRowException.class);
    }

    @Test
    void 존재_유무_확인() {
        User user2 = userService.findByEmail("woowa@woowa.com");
        assertThatThrownBy(() -> {
            commentService.exist(19L, user2);
        }).isInstanceOf(NoPermissionCommentException.class);
    }
}