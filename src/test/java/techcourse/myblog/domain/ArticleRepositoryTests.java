package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleRepositoryTests {
    private static final String SAMPLE_TITLE = "test title";
    private static final String SAMPLE_COVER_URL = "https://techcourse.woowahan.com/images/default/default-cover.jpeg";
    private static final String SAMPLE_CONTENTS = "test contents";

    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
    }

    @Test
    void add() {
        addSampleArticle();

        Article addedArticle = articleRepository.findAll().get(0);
        assertThat(addedArticle.getTitle()).isEqualTo(SAMPLE_TITLE);
        assertThat(addedArticle.getCoverUrl()).isEqualTo(SAMPLE_COVER_URL);
        assertThat(addedArticle.getContents()).isEqualTo(SAMPLE_CONTENTS);
    }

    @Test
    void findById() {
        long id = addSampleArticle();

        Article foundArticle = articleRepository.findById(id);
        assertThat(foundArticle.getTitle()).isEqualTo(SAMPLE_TITLE);
        assertThat(foundArticle.getCoverUrl()).isEqualTo(SAMPLE_COVER_URL);
        assertThat(foundArticle.getContents()).isEqualTo(SAMPLE_CONTENTS);
    }

    @Test
    void deleteById() {
        long id = addSampleArticle();

        articleRepository.deleteById(id);
        assertThrows(IllegalArgumentException.class, () -> articleRepository.findById(id));
    }
    
    private long addSampleArticle() {
        Article article = new Article(SAMPLE_TITLE, SAMPLE_COVER_URL, SAMPLE_CONTENTS);
        return articleRepository.save(article);
    }
}
