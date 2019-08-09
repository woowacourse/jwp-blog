package techcourse.myblog.service.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.comment.CommentRequest;
import techcourse.myblog.service.dto.comment.CommentResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.service.comment.CommentAssembler.convertToDto;
import static techcourse.myblog.service.comment.CommentAssembler.convertToEntity;

public class CommentAssemblerTest {
    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        user = new User("john123@example.com", "john", "p@ssW0rd");
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
        assertThat(convertToDto(comment)).isEqualTo(new CommentResponse(null, "comment", null, "john", LocalDateTime.MIN));
    }
}
