package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.ArticleInputException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleTest {
    private Article article;
    private Long testId = 1L;
    private String testTitle = "Hello World!";
    private String testCoverUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxeXn0MrNFeiv4vBrMSTJWC24F2OZrygOE0h__bEuVXPFANvWB";
    private String invalidCoverUrl = "hello";
    private String testContents = "모두들 안녕!";

    @Test
    void url이_정상적으로_입력됐을_때_테스트() {
        Article testArticle = Article.builder()
                .id(testId)
                .title(testTitle)
                .coverUrl(testCoverUrl)
                .contents(testContents)
                .build();
    }

    @Test
    void url이_정상적으로_입력안됐을_때_테스트() {
        assertThrows(ArticleInputException.class,
                () -> Article.builder()
                        .id(testId)
                        .title(testTitle)
                        .coverUrl(invalidCoverUrl)
                        .contents(testContents)
                        .build());
    }
}
