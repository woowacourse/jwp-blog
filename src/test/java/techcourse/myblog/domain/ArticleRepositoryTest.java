package techcourse.myblog.domain;

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

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void 게시글_정상_추가() {
        Article article = new Article("title", "url", "contents");
        articleRepository.insert(article);
        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    void 게시글_정상_검색() {
        Article article = new Article("title", "url", "contents");
        articleRepository.insert(article);
        assertThat(articleRepository.find(0)).isEqualTo(article);
    }

    @Test
    void 존재하지_않는_게시글_검색_에러() {
        assertThrows(RuntimeException.class, () -> articleRepository.find(0));
    }
}