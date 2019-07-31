package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.dto.ArticleDto;
import techcourse.myblog.web.dto.CommentDto;
import techcourse.myblog.web.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureWebTestClient
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
        author = userService.save(new UserDto("aiden", "aiden1@naver.com", "12Aiden@@"));
        article = articleService.save(new ArticleDto("gogo", "coverUrl", "contents"), author);
        CommentDto commentDto = new CommentDto("brand new comment");
        beforeComment = commentService.save(commentDto, author, article);
    }

    @Test
    void 저장_조회_테스트() {
        Comment comment = commentService.save(new CommentDto("contents"), author, article);
        assertThat(commentService.findById(comment.getId())).isEqualTo(comment);
    }

    @Test
    void 수정_테스트() {
        CommentDto newCommentDto = new CommentDto("update");
        Comment newComment = commentService.update(beforeComment.getId(), newCommentDto);
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
        commentService.delete(beforeCommentId);
        assertThatThrownBy(() -> {
            commentService.findById(beforeCommentId);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}