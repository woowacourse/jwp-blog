package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private Article testArticle;

    @BeforeEach
    void setUp() {
        this.articleRepository = new ArticleRepository();
        this.testArticle = new Article("2", "3",  "4");
        this.articleRepository.add(this.testArticle);
    }

    @Test
    void findAll_호출후_값_조작시_에러_테스트() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.articleRepository.findAll().add(testArticle);
        });
    }

    @Test
    void 아이디값으로_조회_테스트() {
        assertThat(this.testArticle).isEqualTo(this.articleRepository.findById(1));
    }

    @Test
    void article_업데이트_테스트() {
        Article article = new Article("3", "4", "5");
        article.setId(1);
        this.articleRepository.update(1, article);
        assertThat(this.articleRepository.findById(1)).isEqualTo(article);
    }

    @AfterEach
    void tearDown() {
        this.articleRepository.remove(1);
        assertThrows(IllegalArgumentException.class, () -> this.articleRepository.findById(1));
    }
}
