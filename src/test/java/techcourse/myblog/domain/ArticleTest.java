package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {
    private Article article;
    private Long testId = 1L;
    private String testTitle = "Hello World!";
    private String testCoverUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxeXn0MrNFeiv4vBrMSTJWC24F2OZrygOE0h__bEuVXPFANvWB";
    private String testContents = "모두들 안녕!";

    @BeforeEach
    void setUp() {
        article = Article.builder()
                .id(testId)
                .title(testTitle)
                .coverUrl(testCoverUrl)
                .contents(testContents)
                .build();
    }

    @Test
    void 생성_테스트() {
        Article testArticle = Article.builder()
                .id(testId)
                .title(testTitle)
                .coverUrl(testCoverUrl)
                .contents(testContents)
                .build();
        assertThat(article).isEqualTo(testArticle);
    }
}
