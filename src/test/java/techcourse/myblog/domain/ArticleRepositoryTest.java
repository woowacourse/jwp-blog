package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void save() {
        Article article = new Article();
        article.setId(1);
        article.setTitle("title");
        article.setCoverUrl("test");
        article.setContents("test");
        article.setCategoryId(1);

        assertThat(articleRepository.save(article)).isEqualTo(article);
    }

}