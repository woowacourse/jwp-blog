package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CommentRepositoryTest {
    private static final String TEST_TITLE = "Jemok";
    private static final String TEST_COVER_URL = "Baegyung";
    private static final String TEST_CONTENTS = "Naeyong";
    private static final Article TEST_ARTICLE = new Article(TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS);
    private static final String TEST_NAME = "donut";
    private static final String TEST_EMAIL = "donut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    private static final Comment TEST_COMMENT = new Comment(TEST_USER, "ㅎㅇ");

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findById() {
        Comment persistentComment = testEntityManager.persist(TEST_COMMENT);
        TEST_ARTICLE.addComment(persistentComment);
        Article persistentArticle = testEntityManager.persist(TEST_ARTICLE);
        testEntityManager.flush();
        testEntityManager.clear();
        Article actualArticle = articleRepository.findById(persistentArticle.getId()).get();
        assertThat(actualArticle.getComments().size()).isEqualTo(1);
    }
//
//    @Test
//    public void findById2() {
//        _Article _article = new _Article("Zemoc", "MyDragon");
//        _Article _persistentArticle = testEntityManager.persist(_article);
//        _User user = new _User("Donatsu");
//        user.addArticle(_persistentArticle);
//        _User _persistentUser = testEntityManager.persist(user);
//        testEntityManager.flush();
//        testEntityManager.clear();
//        _User _actualUser = _userRepository.findById(_persistentUser.getId()).get();
//        assertThat(_actualUser.getArticles().size()).isEqualTo(0);
//    }
}