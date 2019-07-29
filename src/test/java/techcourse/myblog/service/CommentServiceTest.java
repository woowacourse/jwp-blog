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
    private static final String EMAIL = "aiden1@naver.com";

    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;

    private User user;
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
        user = userService.save(new UserDto("aiden", EMAIL, "aiden3"));
        article = articleService.save(new ArticleDto("gogo", "coverUrl", "contents"));
        CommentDto commentDto = new CommentDto(user, "brand new comment");
        commentDto.setArticle(article);
        beforeComment = commentService.save(commentDto);
    }

    @Test
    void 저장_조회_테스트() {
        Comment comment = commentService.save(new CommentDto(user, "contents"));
        assertThat(commentService.findById(comment.getId())).isEqualTo(comment);
    }

    @Test
    void 수정_테스트() {
        CommentDto newCommentDto = new CommentDto(user, "update");
        Comment newComment = commentService.update(beforeComment.getId(), newCommentDto);
        assertThat(newComment.getContents()).isEqualTo("update");
    }

    @Test
    void 코멘트_목록_테스트() {
        CommentDto commentDto = new CommentDto(user, "brand new comment");
        commentDto.setArticle(article);
        commentService.save(commentDto);
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