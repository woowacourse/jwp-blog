package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {
    @Test
    void modify_test() {
        Article basicArticle = new Article("title", "coverUrl", "contents");
        Article expectModifyArticle = new Article("modifiedTitle", "modifiedCoverUrl", "modifiedContents");
        basicArticle.update(expectModifyArticle);

        assertThat(basicArticle.getTitle()).isEqualTo(expectModifyArticle.getTitle());
        assertThat(basicArticle.getCoverUrl()).isEqualTo(expectModifyArticle.getCoverUrl());
        assertThat(basicArticle.getContents()).isEqualTo(expectModifyArticle.getContents());
    }
}
