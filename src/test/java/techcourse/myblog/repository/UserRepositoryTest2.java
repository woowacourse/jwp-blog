package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest2 {
    private static final String NAME = "아잌아잌";
    private static final String EMAIL = "cony@naver.com";
    private static final String PASSWORD = "@Password1234";

    private static final String AUTHOR_NAME = "아잌아잌";
    private static final String AUTHOR_EMAIL = "ike@ike.com";
    private static final String AUTHOR_PASSWORD = "@Password1234";

    private static final String COMMENTER_NAME = "에헴";
    private static final String COMMENTER_EMAIL = "ehem@ehem.com";
    private static final String COMMENTER_PASSWORD = "@Password1234";

    private static final String TITLE = "달";
    private static final String CONTENTS = "\uDF19";
    private static final String COVER_URL = "";

    private static final String COMMENT = "내가 쓴 글~";

    private User author;
    private User commenter;
    private Article article;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        author = new User(AUTHOR_NAME, AUTHOR_PASSWORD, AUTHOR_EMAIL);
        commenter = new User(COMMENTER_NAME, COMMENTER_PASSWORD, COMMENTER_EMAIL);
        article = new Article(TITLE, CONTENTS, COVER_URL);
    }

    /*@Test
    public void findById() {
        User user = new User(NAME, PASSWORD, EMAIL);
        User persistUser = testEntityManager.persist(user);

        Article article = new Article(TITLE, CONTENTS, COVER_URL);
        article.setAuthor(persistUser);
        testEntityManager.persist(article);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(1);
    }

    @Test
    public void findById2() {
        Article article = new Article(TITLE, CONTENTS, COVER_URL);
        Article persistArticle = testEntityManager.persist(article);

        User user = new User(NAME, PASSWORD, EMAIL);
        user.addArticle(persistArticle);
        User persistUser = testEntityManager.persist(user);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(0);
    }*/

    private void cleanUpTestEntityManager() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}