package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {
    private static final Long BASE_USER_ID = 1L;
    private static final Long MISMATCH_USER_ID = 2L;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    private Long articleId;

    @BeforeEach
    public void setUp() {
        ArticleDto articleDto = articleService.save(BASE_USER_ID,
                new ArticleDto(null, BASE_USER_ID, "title", "url", "contents"));
        articleId = articleDto.getId();
    }

    @Test
    public void saveComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);

        assertThat(comment.getComment()).isEqualTo(commentRequestDto.getComment());
    }

    @Test
    public void findCommentsByArticleId() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);
        List<CommentResponseDto> comments = commentService.findCommentsByArticleId(articleId);

        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getComment()).isEqualTo(comment.getComment());
        assertThat(comments.get(0).getAuthorName()).isEqualTo(comment.getAuthorName());
    }

    @Test
    public void updateComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);
        CommentRequestDto updateRequestDto = new CommentRequestDto(articleId, "UPDATE Comment");
        Comment updatedComment = commentService.update(BASE_USER_ID, comment.getId(), updateRequestDto);

        assertThat(updatedComment.getComment()).isEqualTo("UPDATE Comment");
    }

    @Test
    public void deleteComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);

        commentService.delete(BASE_USER_ID, comment.getId());
        assertThat(commentService.findCommentsByArticleId(articleId).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Comment를 등록한 User가 다를때 수정 실패")
    public void failUpdatingCommentWhenMismatchUser() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);
        CommentRequestDto updateRequestDto = new CommentRequestDto(articleId, "UPDATE Comment");
        Comment updateComment = commentService.update(MISMATCH_USER_ID, comment.getId(), updateRequestDto);

        assertThat(updateComment.getComment()).isEqualTo("TEST Comment");
    }

    @Test
    @DisplayName("Comment를 등록한 User가 다를때 삭제 실패")
    public void failDeletingCommentWhenMismatchUser() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);

        commentService.delete(MISMATCH_USER_ID, comment.getId());
        assertThat(commentService.findCommentsByArticleId(articleId).size()).isEqualTo(1);
    }
}