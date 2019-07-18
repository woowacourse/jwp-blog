package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.ArticleToUpdateNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ArticleTest {
    @Test
    void 생성자_확인() {
        assertThat(new Article("title", "", "content"))
                .isEqualTo(new Article("title", "", "content"));
    }

    @Test
    void 생성자_오류확인_title이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article(null, "", "content"));
    }

    @Test
    void 생성자_오류확인_coverUrl이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article("title", null, "content"));
    }

    @Test
    void 생성자_오류확인_contents가_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article("title", "", null));
    }

    @Test
    void 게시글_수정_확인() {
        Article oldArticle = new Article("title", "", "content");
        Article newArticle = new Article("newTitle", "", "newContent");
        oldArticle.update(newArticle);
        assertThat(oldArticle).isEqualTo(newArticle);
    }

    @Test
    void 게시글_수정_오류확인_인자가_null인_경우() {
        Article article = new Article("title", "", "content");
        assertThatExceptionOfType(ArticleToUpdateNotFoundException.class)
                .isThrownBy(() -> article.update(null))
                .withMessage("업데이트 해야할 게시글이 없습니다.");
    }
}
