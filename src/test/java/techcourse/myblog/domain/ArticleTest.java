package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    @Test
    void 게시글_정상_생성() {
        assertDoesNotThrow(() -> new Article(0, "testTitle", "testUrl", "testContents"));
    }

    @Test
    void 제목이_비어있는_경우_에러_발생() {
        assertThrows(InvalidArticleException.class,
                () -> new Article(0, "", "testUrl", "testContents"));
    }

    @Test
    void 제목이_null인_경우_에러_발생() {
        assertThrows(InvalidArticleException.class,
                () -> new Article(0, null, "testUrl", "testContents"));
    }

    @Test
    void 내용이_비어있는_경우_에러_발생() {
        assertThrows(InvalidArticleException.class,
                () -> new Article(0, "testTitle", "testUrl", ""));
    }

    @Test
    void 내용이_null인_경우_에러_발생() {
        assertThrows(InvalidArticleException.class,
                () -> new Article(0, "testTitle", "testUrl", null));
    }

    @Test
    void 배경url이_비어있는_경우_게시글_정상_생성() {
        assertDoesNotThrow(() -> new Article(0, "testTitle", "", "testContents"));
    }

    @Test
    void 배경url이_null인_경우_게시글_정상_생성() {
        assertDoesNotThrow(() -> new Article(0, "testTitle", null, "testContents"));
    }

    @Test
    void 게시글_정상_수정() {
        Article sourceArticle = new Article(0, "title0", "url0", "contents0");
        Article targetArticle = new Article(0, "title1", "url1", "contents1");

        sourceArticle.update(targetArticle);
        assertEquals(sourceArticle, targetArticle);
    }
}