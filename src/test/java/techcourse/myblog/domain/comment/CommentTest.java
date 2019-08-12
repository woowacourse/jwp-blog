package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.application.CommentAssembler;
import techcourse.myblog.application.UserAssembler;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.exception.InvalidCommentException;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_FEATURE;
import static techcourse.myblog.utils.CommentTestObjects.*;
import static techcourse.myblog.utils.UserTestObjects.AUTHOR_DTO;

public class CommentTest {
    private User author = UserAssembler.toEntity(AUTHOR_DTO);
    private Article article = ARTICLE_FEATURE.toArticle(author);

    @Test
    void 댓글_정상_생성() {
        assertDoesNotThrow(() -> CommentAssembler.toEntity(COMMENT_DTO, author, article));
    }

    @Test
    void 댓글_생성_불가1() {
        assertThrows(InvalidCommentException.class, () -> CommentAssembler.toEntity(BLANK_COMMENT_DTO, author, article));
    }

    @Test
    void 댓글_생성_불가2() {
        assertThrows(InvalidCommentException.class, () -> CommentAssembler.toEntity(NULL_COMMENT_DTO, author, article));
    }

    @Test
    void 댓글_업데이트() {
        Comment originalComment = CommentAssembler.toEntity(COMMENT_DTO, author, article);
        Comment editedComment = CommentAssembler.toEntity(UPDATE_COMMENT_DTO, author, article);

        originalComment.update(editedComment);

        assertThat(originalComment.getContents()).isEqualTo(editedComment.getContents());
    }

    @Test
    void 댓글_업데이트_불가1() {
        Comment originalComment = CommentAssembler.toEntity(COMMENT_DTO, author, article);
        assertThrows(InvalidCommentException.class, () -> originalComment.update(null));
    }
}
