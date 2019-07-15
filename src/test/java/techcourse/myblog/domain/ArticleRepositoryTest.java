package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
    String title = "제목";
    String coverUrl = "링크";
    String contents = "내용";

    @Autowired
    private ArticleRepository articleRepository;

    Article testArticle = new Article();

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
        testArticle.setTitle(title);
        testArticle.setCoverUrl(coverUrl);
        testArticle.setContents(contents);
    }

    @Test
    void write() {
        articleRepository.write(testArticle);
        assertThat(
                articleRepository.findAll().isEmpty()
        ).isFalse();
    }

    @Test
    void find() {
        articleRepository.write(testArticle);
        assertThrows(IllegalArgumentException.class, () -> {
           articleRepository.find(2);
        });
    }

    @Test
    void edit() {
        articleRepository.write(testArticle);
        articleRepository.edit(new Article(), articleRepository.getLastArticleId());
        assertThat(
                articleRepository.find(articleRepository.getLastArticleId()).getCoverUrl()
        ).isEqualTo("");
    }

    @Test
    void delete() {
        articleRepository.write(testArticle);
        articleRepository.delete(1);
        assertThat(
            articleRepository.findAll().size()
        ).isZero();
    }
}