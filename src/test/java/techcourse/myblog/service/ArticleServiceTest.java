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
import techcourse.myblog.service.exception.NotFoundCommentException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static techcourse.myblog.Utils.TestConstants.BASE_USER_ID;
import static techcourse.myblog.Utils.TestConstants.MISMATCH_USER_ID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    private Long articleId;

    @BeforeEach
    void setUp() {
        ArticleDto articleDto = articleService.save(
                BASE_USER_ID, new ArticleDto(null, BASE_USER_ID, "title", "coverUrl", "contents"));
        articleId = articleDto.getId();
    }

    @Test
    void Article_userId와_수정하려는_User의_Id가_다르면_수정_실패() {
        ArticleDto updateArticleDto =
                new ArticleDto(articleId, BASE_USER_ID, "title1", "coverUrl1", "contents1");

        assertThatThrownBy(() -> articleService.update(articleId, MISMATCH_USER_ID, updateArticleDto))
                .isInstanceOf(UserMismatchException.class);
    }

    @Test
    void Article_userId와_삭제하려는_User의_Id가_다르면_삭제_실패() {
        assertThatThrownBy(() -> articleService.delete(articleId, MISMATCH_USER_ID))
                .isInstanceOf(UserMismatchException.class);
    }

    @Test
    @DisplayName("Article을 삭제했을 때 포함된 Comment도 삭제된다")
    void deleteArticleWithCascadeComments() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
        Comment comment = commentService.save(BASE_USER_ID, commentRequestDto);

        articleService.delete(articleId, BASE_USER_ID);

        assertThatThrownBy(() -> commentService.findById(comment.getId()))
                .isInstanceOf(NotFoundCommentException.class);
    }
}
