package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleTest {

    @Test
    void 게시글_정상_생성() {
        assertDoesNotThrow(() -> new Article(0, "testTitle", "testUrl", "testContents"));
    }

    @Test
    void 제목이_비어있는_경우_에러_발생() {
        assertThrows(IllegalArgumentException.class,
                () -> new Article(0, "", "testUrl", "testContents"));
    }

    @Test
    void 제목이_null인_경우_에러_발생() {
        assertThrows(IllegalArgumentException.class,
                () -> new Article(0, null, "testUrl", "testContents"));
    }

    @Test
    void 내용이_비어있는_경우_에러_발생() {
        assertThrows(IllegalArgumentException.class,
                () -> new Article(0, "testTitle", "testUrl", ""));
    }

    @Test
    void 내용이_null인_경우_에러_발생() {
        assertThrows(IllegalArgumentException.class,
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
}