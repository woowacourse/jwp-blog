package techcourse.myblog.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void findById() {
        User user = new User("Moomin", "123!@#qweQWE", "moomin@woowahan.com");
        User persistUser = testEntityManager.persist(user);

        Article article = new Article("Moomin's article", "Happy Jpa", "hihi");
        Article article2 = new Article("Ko's article", "Sad Jpa", "heyhey");
        article.setAuthor(persistUser);
        article2.setAuthor(persistUser);
        testEntityManager.persist(article);
        testEntityManager.persist(article2);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(articleRepository.findAllByAuthor(actualUser).size()).isEqualTo(2);
    }
}