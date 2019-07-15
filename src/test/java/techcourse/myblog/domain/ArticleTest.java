package techcourse.myblog.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleTest {

    private static final long ID = 1L;
    private static final String TITLE = "TITLE";
    private static final String COVER_URL = "https://url.com";
    private static final String CONTENTS = "CONTENTS";
    private static final Article SAMPLE_ARTICLE = new Article(ID, TITLE, COVER_URL, CONTENTS);

    @Test
    public void 동시성_테스트() {
        Article expected = SAMPLE_ARTICLE;
        Article actual = new Article(ID, TITLE, COVER_URL, CONTENTS);
        assertEquals(expected, actual);
    }

    @Test
    public void 동시성_테스트_다를때() {
        long diffId = 3L;
        Article expected = SAMPLE_ARTICLE;
        Article actual = new Article(diffId, TITLE, COVER_URL, CONTENTS);
        assertNotEquals(expected, actual);
    }

    @Test
    public void ID가_같을때_True를_반환하는지_테스트() {
        assertTrue(SAMPLE_ARTICLE.isSameId(ID));
    }

    @Test
    public void ID가_다를때_False를_반환하는지_테스트() {
        long id = 2L;
        assertFalse(SAMPLE_ARTICLE.isSameId(id));
    }

    @Test
    @DisplayName("Article에_대한_변경_테스트")
    public void update() {
        Article expected = SAMPLE_ARTICLE;
        Article actual = new Article(2L, "NEED CHANGE", "http://urlurl.com"
                , "Content need change");
        actual.update(expected);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Dto_변환_테스트")
    public void toDto() {
        ArticleDto expected = new ArticleDto(ID, TITLE, COVER_URL, CONTENTS);
        ArticleDto actual = SAMPLE_ARTICLE.toDto();

        assertEquals(expected, actual);
    }
}