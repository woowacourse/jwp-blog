package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleRepositoryTests {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void add() {
        Article article = new Article();
        article.setContents("Contents");
        article.setCoverUrl("https://www.woowa.com");
        article.setTitle("Hi");
        articleRepository.add(article);
        assertThat(articleRepository.findAll().get(0).getContents().equals("Contents"));
        assertThat(articleRepository.findAll().get(0).getCoverUrl().equals("https://www.woowa.com"));
        assertThat(articleRepository.findAll().get(0).getTitle().equals("Hi"));
    }
}
