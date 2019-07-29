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

    @Test
    public void findById() {
        User user = new User("Moomin", "123!@#qweQWE", "moomin@woowahan.com");
        User persistUser = testEntityManager.persist(user);

        Article article = new Article("Moomin's article", "Happy Jpa", "hihi");
        article.setAuthor(persistUser);
        testEntityManager.persist(article);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(1);
    }

    @Test
    public void findById2() {
        Article article = new Article("Moomin's article", "Happy Jpa", "hihi");
        Article persistArticle = testEntityManager.persist(article);

        User user = new User("Moomin", "123!@#qweQWE", "moomin@woowahan.com");
        user.addArticle(persistArticle);  // 주인이 아닌 엔티티로 해봐야 데이터베이스에 반영이 안 됨. 영속성 컨텍스트에서는 반영이 됨?

        User persistUser = testEntityManager.persist(user);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(0);  // 위에서 article 을 추가했음에도 불구하고 데이터베이스에 반영되지 않았음
    }

}