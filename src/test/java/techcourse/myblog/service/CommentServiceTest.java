package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    private Long articleId;

    @BeforeEach
    void setUp() {
        ArticleDto articleDto = articleService.save(1L,
                new ArticleDto(null, 1L, "title", "url", "contents"));
        articleId = articleDto.getId();
    }

    @Test
    void saveComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(1L, commentRequestDto);

        assertThat(comment.getComment()).isEqualTo(commentRequestDto.getComment());
    }

    @Test
    void findCommentsByArticleId() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(1L, commentRequestDto);
        List<CommentResponseDto> comments = commentService.findCommentsByArticleId(articleId);

        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getComment()).isEqualTo(comment.getComment());
        assertThat(comments.get(0).getAuthorName()).isEqualTo(comment.getAuthorName());
    }

    @Test
    void updateComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(1L, commentRequestDto);
        CommentRequestDto updateRequestDto = new CommentRequestDto(articleId, "UPDATE Comment");
        Comment updatedComment = commentService.update(1L, comment.getId(), updateRequestDto);

        assertThat(updatedComment.getComment()).isEqualTo("UPDATE Comment");
    }

    @AfterEach
    void tearDown() {
        commentService.deleteAll();
    }
}