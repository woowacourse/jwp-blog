package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CommentRepositoryTest {
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    private static final String TEST_TITLE = "Jemok";
    private static final String TEST_COVER_URL = "Baegyung";
    private static final String TEST_CONTENTS = "Naeyong";
    private static final Article TEST_ARTICLE = new Article(TEST_USER, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS);
    private static final String _TEST_EMAIL = "test" + TEST_EMAIL;
    private static final User _TEST_USER = new User(TEST_NAME, _TEST_EMAIL, TEST_PASSWORD);

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void commentWriteFindByArticleTest() {
        User persistentUser = testEntityManager.persist(TEST_USER);
        Comment persistentComment = testEntityManager.persist(new Comment(TEST_ARTICLE, persistentUser, "ㅎㅇ"));
        TEST_ARTICLE.writeComment(persistentComment);
        Article persistentArticle = testEntityManager.persist(TEST_ARTICLE);
        testEntityManager.flush();
        testEntityManager.clear();
        Article actualArticle = articleRepository.findById(persistentArticle.getId()).get();
        assertThat(actualArticle.getComments().size()).isEqualTo(1);
    }

    @Test
    public void commentWriteFindByCommentTest() {
        User persistentUser = testEntityManager.persist(_TEST_USER);
        Comment persistentComment = testEntityManager.persist(new Comment(TEST_ARTICLE, persistentUser, "ㅎㅇ"));
        testEntityManager.flush();
        testEntityManager.clear();
        Comment actualComment = commentRepository.findById(persistentComment.getId()).get();
        assertThat(actualComment.getAuthor().getEmail()).isEqualTo(_TEST_EMAIL);
    }
}