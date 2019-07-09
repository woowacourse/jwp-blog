package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleTest {
    @Test
    void 문자열_길이_충분_테스트() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            stringBuilder.append(Collections.nCopies(i, "andole"));
        }

        Article article = new Article("andole", stringBuilder.toString());
        System.out.println(stringBuilder.toString().length());
        assertEquals(stringBuilder.toString(), article.getContents());
    }
}