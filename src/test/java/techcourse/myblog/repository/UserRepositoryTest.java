package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.template.StaticVariableTemplate;
import techcourse.myblog.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest extends StaticVariableTemplate {
    private User user;
    private Article article;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        user = new User(AUTHOR_NAME, AUTHOR_PASSWORD, AUTHOR_EMAIL);
        article = new Article(TITLE, CONTENTS, COVER_URL);
    }

    @Test
    public void findById() {
        // given
        User persistUser = testEntityManager.persist(user);
        testEntityManager.persist(article);

        cleanUpTestEntityManager();

        // when
        Optional<User> actualUser = userRepository.findById(persistUser.getId());

        // then
        actualUser.ifPresent(a -> assertThat(a).isEqualTo(persistUser));
    }

    private void cleanUpTestEntityManager() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}