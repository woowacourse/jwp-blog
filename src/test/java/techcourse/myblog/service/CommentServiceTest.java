package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.exception.NotMatchAuthorException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentServiceTest extends ServiceTest {
    @BeforeEach
    void setUp() {
        init();
    }

    @AfterEach
    void tearDown() {
        terminate();
    }

    @Test
    void 댓글_전체_조회_테스트() {
        assertThat(commentService.findAllByArticle(articleId)).isEqualTo(Arrays.asList(commentDto));
    }

    @Test
    void 댓글_수정_테스트() {
        Comment updateComment = Comment.builder()
            .contents("updated")
            .article(article)
            .user(user)
            .regDate(LocalDateTime.now())
            .modfiedDate(LocalDateTime.now())
            .build();

        CommentDto.Update updateCommentDto = modelMapper.map(updateComment, CommentDto.Update.class);
        updateCommentDto.setArticleId(articleId);

        CommentDto.Response newCommentDto = commentService.update(userDto, commentId, updateCommentDto);

        assertThat(commentService.findAllByArticle(articleId)).isEqualTo(Arrays.asList(newCommentDto));
    }

    @Test
    void 다른_작성자_댓글_수정_테스트() {
        Comment updateComment = Comment.builder()
            .contents("updated")
            .article(article)
            .user(user)
            .regDate(LocalDateTime.now())
            .modfiedDate(LocalDateTime.now())
            .build();

        CommentDto.Update updateCommentDto = modelMapper.map(updateComment, CommentDto.Update.class);
        assertThrows(NotMatchAuthorException.class, () -> commentService.update(otherUserDto, commentId, updateCommentDto));
    }

    @Test
    void 다른_작성자_삭제_실패_테스트() {
        assertThrows(NotMatchAuthorException.class, () -> commentService.deleteById(otherUserDto, commentId));
    }
}