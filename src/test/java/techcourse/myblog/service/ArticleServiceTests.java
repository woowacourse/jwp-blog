package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTests {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;
    @Test
    void save_Test() {
        Article article =Article.builder()
                .title("a")
                .coverUrl("b")
                .contents("c")
                .build();
        articleService.save(article);
        assertThat(articleRepository.findById((long)1)).isNotNull();
    }

    @Test
    void delete_Test(){
        Article article =  Article.builder()
                .title("a")
                .coverUrl("b")
                .contents("c")
                .build();
        articleService.save(article);
        articleService.delete((long)1);
        assertThat(articleRepository.findById((long)1).isPresent()).isEqualTo(false);
    }
}
