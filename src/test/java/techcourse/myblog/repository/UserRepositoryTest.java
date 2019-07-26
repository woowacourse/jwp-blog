package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    private static final String EMAIL = "test1@test.com";
    private static final String PASSWORD = "!Q@W3e4r";
    private static final String USERNAME = "test1";

    private static final String ARTICLE_TITLE = "title";
    private static final String ARTICLE_CONTENT = "content";
    private static final String ARTICLE_COVER_URL = "coverUrl";

    private User user;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;



    @BeforeEach
    void setUp() {
        user = new User(USERNAME, EMAIL, PASSWORD);
        user = userRepository.save(user);
    }

    @Test
    void 유저_이메일_정보로_유저_찾기() {
        assertThat(userRepository.existsByEmail(EMAIL)).isTrue();
    }

    @Test
    void 유저_삭제() {
        userRepository.deleteAll();
        assertThat(userRepository.existsByEmail(EMAIL)).isFalse();
    }

    @Test
    public void findById() {
        Article article = new Article(ARTICLE_TITLE, ARTICLE_CONTENT, ARTICLE_COVER_URL);
        article.setAuthor(user);
        testEntityManager.persist(article);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(user.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(1);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
