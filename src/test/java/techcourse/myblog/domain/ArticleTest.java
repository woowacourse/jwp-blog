package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ArticleTest {
    @Test
    void 생성자_확인() {
        assertThat(new Article(1, "title", "", "content"))
                .isEqualTo(new Article(1, "title", "", "content"));
    }

    @Test
    void 생성자_오류확인_id가_양수가_아닐_경우() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Article(0, "title", "", "content"))
                .withMessage("적절한 ID가 아닙니다.");
    }

    @Test
    void 생성자_오류확인_title이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article(1, null, "", "content"));
    }

    @Test
    void 생성자_오류확인_coverUrl이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article(1, "title", null, "content"));
    }

    @Test
    void 생성자_오류확인_contents가_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Article(1, "title", "", null));
    }

    @Test
    void 게시글_id와_같은지_확인() {
        Article article = new Article(1, "title", "", "content");
        assertThat(article.match(1)).isTrue();
    }

    @Test
    void 게시글_id와_다른지_확인() {
        Article article = new Article(1, "title", "", "content");
        assertThat(article.match(2)).isFalse();
    }

    @Test
    void 게시글_수정_확인() {
        Article oldArticle = new Article(1, "title", "", "content");
        Article newArticle = new Article(1, "newTitle", "", "newContent");
        oldArticle.update(newArticle);
        assertThat(oldArticle).isEqualTo(newArticle);
    }

    @Test
    void 게시글_수정_오류확인_인자가_null인_경우() {
        Article article = new Article(1, "title", "", "content");
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> article.update(null));
    }
}
