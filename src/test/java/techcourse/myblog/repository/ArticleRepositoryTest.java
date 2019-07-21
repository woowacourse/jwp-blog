package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        Article article1 = new Article("a1", "b", "c");
        Article article2 = new Article("a2", "b", "c");
        Article article3 = new Article("a3", "b", "c");

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }

    @Test
    void createTest() {
        long count = articleRepository.count();
        articleRepository.save(new Article("title", "contents", "coverUrl"));
        assertThat(articleRepository.count()).isNotEqualTo(count);
    }

    @Test
    void findByIdTest() {
        assertThatThrownBy(() -> articleRepository
                .findById(100L).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllTest() {
        List<Article> list = new ArrayList<>();
        articleRepository.findAll().forEach(list::add);

        assertThat(list.get(0).getTitle()).isEqualTo("a1");
        assertThat(list.get(1).getTitle()).isEqualTo("a2");
        assertThat(list.get(2).getTitle()).isEqualTo("a3");
    }

    @Test
    void UpdateTest() {
        Article article = new Article("test", "test", "test");
        articleRepository.save(article);
        ArticleDto articleDto = new ArticleDto("a100", "edit", "edit");
        long id = article.getId();
        article.update(articleDto.toEntity());
        Article post = articleRepository.findById(id).orElseThrow(IllegalAccessError::new);

        assertThat(post.getTitle()).isEqualTo(article.getTitle());
        assertThat(post.getContents()).isNotEqualTo("test");
        assertThat(post.getCoverUrl()).isEqualTo("edit");
    }

    @Test
    void deleteTest() {
        Article article = new Article("test", "test", "test");
        articleRepository.save(article);
        long id = article.getId();
        articleRepository.deleteById(id);
        assertThatThrownBy(() -> articleRepository.findById(id).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}