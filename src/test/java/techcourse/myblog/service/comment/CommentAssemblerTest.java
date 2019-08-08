package techcourse.myblog.service.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.article.Article;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.dto.CommentRequest;
import techcourse.myblog.comment.dto.CommentResponse;
import techcourse.myblog.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.comment.service.CommentAssembler.convertToDto;
import static techcourse.myblog.comment.service.CommentAssembler.convertToEntity;

public class CommentAssemblerTest {
    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        user = new User(999L, "john123@example.com", "john", "p@ssW0rd");
        article = new Article("title", "", "contents", user);
    }

    @Test
    void dto를_entity로_변환하는지_확인() {
        CommentRequest commentRequest = new CommentRequest("comment");
        assertThat(convertToEntity(commentRequest, user, article)).isEqualTo(new Comment("comment", user, article));
    }

    @Test
    void entity를_dto로_변환하는지_확인() {
        Comment comment = new Comment("comment", user, article);
        assertThat(convertToDto(comment)).isEqualTo(new CommentResponse(null, "comment", null, "john", null, LocalDateTime.MIN));
    }
}
