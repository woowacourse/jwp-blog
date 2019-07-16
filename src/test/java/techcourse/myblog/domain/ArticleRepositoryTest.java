package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @Test
    void save() {
        Article article = Article.builder()
                .title("title")
                .contents("contents")
                .coverUrl("coverUrl")
                .build();

        assertThat(articleRepository.save(article)).isNotNull();
    }
}
