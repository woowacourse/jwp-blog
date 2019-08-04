package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    private Long userId;
    private Long articleId;
    private Long commentId;

    @BeforeEach
    void setUp() {
        UserDto.Register userDto = UserDto.Register.builder()
                .email("email@gmail.com")
                .name("name")
                .password("P@ssw0rd")
                .confirmPassword("P@ssw0rd")
                .build();

        userId = userService.save(userDto);

        ArticleDto.Request articleDto = ArticleDto.Request.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build();

        articleId = articleService.save(userId, articleDto);

        CommentDto.Create commentDto = new CommentDto.Create();
        commentDto.setArticleId(articleId);
        commentDto.setContents("contents");

        commentId = commentService.save(commentDto, userId).getId();
    }

    @Test
    void update() {
        final String modifiedContents = "updateContents";

        CommentDto.Update commentDto = new CommentDto.Update();
        commentDto.setId(commentId);
        commentDto.setArticleId(articleId);
        commentDto.setContents(modifiedContents);

        final CommentDto.Response modifiedComment = commentService.update(commentDto, userId);

        assertThat(modifiedContents).isEqualTo(modifiedComment.getContents());
    }
}