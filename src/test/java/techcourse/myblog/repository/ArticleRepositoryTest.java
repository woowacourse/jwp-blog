package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private long userId;

    @BeforeEach
    void setUp() {
        user = userRepository.findByEmailEmail("test@test.com").get();
    }

    @Test
    void createTest() {
        long count = articleRepository.count();
        Article article = articleRepository.save(new Article("title", "contents", "coverUrl", user));
        assertThat(articleRepository.count()).isNotEqualTo(count);
        articleRepository.delete(article);
    }

    @Test
    void findByIdTest() {
        assertThatThrownBy(() -> articleRepository
                .findById(100L).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllTest() {
        Article article1 = new Article("a1", "b", "c", user);
        Article article2 = new Article("a2", "b", "c", user);
        Article article3 = new Article("a3", "b", "c", user);

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);

        List<Article> list = articleRepository.findAll();
        assertThat(list.stream().anyMatch(article -> article.getTitle().equals("a1"))).isTrue();
        assertThat(list.stream().anyMatch(article -> article.getTitle().equals("a2"))).isTrue();
        assertThat(list.stream().anyMatch(article -> article.getTitle().equals("a3"))).isTrue();

        articleRepository.delete(article1);
        articleRepository.delete(article2);
        articleRepository.delete(article3);
    }

    @Test
    void UpdateTest() {
        Article article = new Article("test", "test", "test", user);
        articleRepository.save(article);

        article.update(new Article("update", "update", "update", user));
        Article post = articleRepository.findById(article.getId()).orElseThrow(IllegalAccessError::new);

        assertThat(post.getTitle()).isEqualTo(article.getTitle());
        assertThat(post.getContents()).isNotEqualTo("test");
        assertThat(post.getCoverUrl()).isEqualTo("update");

        articleRepository.delete(article);
    }

    @Test
    void deleteTest() {
        Article article = new Article("test", "test", "test", user);
        articleRepository.save(article);
        long id = article.getId();
        articleRepository.deleteById(id);
        assertThatThrownBy(() -> articleRepository.findById(id).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}