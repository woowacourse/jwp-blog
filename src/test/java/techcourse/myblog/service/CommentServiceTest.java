package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.exception.UserMismatchException;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static techcourse.myblog.Utils.TestConstants.BASE_USER_ID;
import static techcourse.myblog.Utils.TestConstants.MISMATCH_USER_ID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    private Long articleId;

    @BeforeEach
    void setUp() {
        ArticleDto articleDto = articleService.save(BASE_USER_ID,
                new ArticleDto(null, BASE_USER_ID, "title", "url", "contents"));
        articleId = articleDto.getId();
    }

    @Test
    void saveComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);

        assertThat(comment.getComment()).isEqualTo(commentRequestDto.getComment());
        assertThat(comment.matchAuthorId(BASE_USER_ID)).isTrue();
    }

    @Test
    void findCommentsByArticleId() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);
        List<CommentResponseDto> comments = commentService.findCommentsByArticleId(articleId);

        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getComment()).isEqualTo(comment.getComment());
        assertThat(comments.get(0).getAuthorName()).isEqualTo(comment.getAuthorName());
    }

    @Test
    void updateComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);
        CommentRequestDto updateRequestDto = new CommentRequestDto(articleId, "UPDATE Comment");
        Comment updatedComment = commentService.update(BASE_USER_ID, comment.getId(), updateRequestDto);

        assertThat(updatedComment.getComment()).isEqualTo("UPDATE Comment");
    }

    @Test
    void deleteComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);

        commentService.delete(BASE_USER_ID, comment.getId());
        assertThat(commentService.findCommentsByArticleId(articleId).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Comment를 등록한 User가 다를때 수정 실패")
    void failUpdatingCommentWhenMismatchUser() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);
        CommentRequestDto updateRequestDto = new CommentRequestDto(articleId, "UPDATE Comment");

        assertThatThrownBy(() -> commentService.update(MISMATCH_USER_ID, comment.getId(), updateRequestDto))
                .isInstanceOf(UserMismatchException.class);
    }

    @Test
    @DisplayName("Comment를 등록한 User가 다를때 삭제 실패")
    void failDeletingCommentWhenMismatchUser() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);

        assertThatThrownBy(() -> commentService.delete(MISMATCH_USER_ID, comment.getId()))
                .isInstanceOf(UserMismatchException.class);
    }
}