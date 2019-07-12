package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleRepositoryTests {
    private static final String SAMPLE_TITLE = "test title";
    private static final String SAMPLE_COVER_URL = "https://techcourse.woowahan.com/images/default/default-cover.jpeg";
    private static final String SAMPLE_CONTENTS = "test contents";

    @Autowired
    private ArticleRepository articleRepository;

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
        articleRepository.add(new Article());
        articleRepository.add(new Article());
        long id = addSampleArticle();

        Article foundArticle = articleRepository.findById(id);
        assertThat(foundArticle.getTitle()).isEqualTo(SAMPLE_TITLE);
        assertThat(foundArticle.getCoverUrl()).isEqualTo(SAMPLE_COVER_URL);
        assertThat(foundArticle.getContents()).isEqualTo(SAMPLE_CONTENTS);
    }

    @Test
    void deleteById() {
        articleRepository.add(new Article());
        articleRepository.add(new Article());
        long id = addSampleArticle();

        articleRepository.deleteById(id);
        assertThrows(IllegalArgumentException.class, () -> articleRepository.findById(id));
    }

    @AfterEach
    void tearDown() {
        articleRepository.clear();
    }

    private long addSampleArticle() {
        Article article = new Article();
        article.setTitle(SAMPLE_TITLE);
        article.setCoverUrl(SAMPLE_COVER_URL);
        article.setContents(SAMPLE_CONTENTS);
        articleRepository.add(article);
        return article.getId();
    }
}
