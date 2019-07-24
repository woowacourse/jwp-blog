package techcourse.myblog.domain.article;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleTest {

    private static final long ID = 1L;
    private static final String TITLE = "TITLE";
    private static final String COVER_URL = "https://url.com";
    private static final String CONTENTS = "CONTENTS";
    private static final Article SAMPLE_ARTICLE = new Article(TITLE, COVER_URL, CONTENTS);

    static {
        SAMPLE_ARTICLE.setId(ID);
    }

    @Test
    public void 동시성_테스트() {
        Article expected = SAMPLE_ARTICLE;
        Article actual = new Article(TITLE, COVER_URL, CONTENTS);
        assertEquals(expected, actual);
    }

    @Test
    public void 동시성_테스트_다를때() {
        Article expected = SAMPLE_ARTICLE;
        Article actual = new Article(TITLE + "differ", COVER_URL, CONTENTS);
        assertNotEquals(expected, actual);
    }

    @Test
    public void ID가_같을때_참값을_반환하는지() {
        assertTrue(SAMPLE_ARTICLE.isSameId(ID));
    }

    @Test
    public void ID가_다를때_거짓값을_반환하는지() {
        long id = 2L;
        assertFalse(SAMPLE_ARTICLE.isSameId(id));
    }

    @Test
    @DisplayName("Article_갱신")
    public void update() {
        Article actual = new Article(1L, "NEED CHANGE", "http://urlurl.com"
                , "Content need change");
        Article expected = SAMPLE_ARTICLE;
        actual.update(expected);

        assertEquals(expected, actual);
    }
}