package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
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

    @Test
    void createTest() {
        long count = articleRepository.count();
        articleRepository.save(new Article("title", "contents", "coverUrl"));
        assertThat(articleRepository.count()).isNotEqualTo(count);
    }

    @Test
    void findByIdTest() {
        assertThat(articleRepository.findById(1L).get().getTitle()).isEqualTo("a1");
        assertThat(articleRepository.findById(1L).get().getId()).isEqualTo(1L);
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
        ArticleDto articleDto = new ArticleDto("a100", "b", "c");
        Article article = articleRepository.findById(1L).orElseThrow(IllegalAccessError::new);
        article.update(articleDto.toEntity());
        articleRepository.save(article);

        assertThat(articleRepository.findById(1L).get().getTitle()).isEqualTo("a100");
    }

    @Test
    void deleteTest() {
        articleRepository.deleteById(1L);
        assertThatThrownBy(() -> articleRepository.findById(1L).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}