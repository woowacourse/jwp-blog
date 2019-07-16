package techcourse.myblog.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleDtoTest {

    private static final long ID = 1L;
    private static final String TITLE = "TITLE";
    private static final String COVER_URL = "http://test.com";
    private static final String CONTENTS = "CONTENTS";
    private static final ArticleDto SAMPLE = new ArticleDto(ID, TITLE, COVER_URL, CONTENTS);

    @Test
    public void 동시성_테스트() {
        ArticleDto expected = SAMPLE;
        ArticleDto actual = new ArticleDto(ID, TITLE, COVER_URL, CONTENTS);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Dto변환_테스트")
    public void toDto() {
        Article sample = new Article(ID, TITLE, COVER_URL, CONTENTS);
        ArticleDto expected = SAMPLE;
        ArticleDto actual = ArticleDto.toDto(sample);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Article클래스로_변환_테스트")
    public void toArticle() {
        Article expected = new Article(ID, TITLE, COVER_URL, CONTENTS);
        Article actual = SAMPLE.toArticle(ID);

        assertEquals(expected, actual);
    }
}