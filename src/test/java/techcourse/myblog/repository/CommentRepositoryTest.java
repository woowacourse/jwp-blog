package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTest {
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
    private Comment comment;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        author = new User(AUTHOR_NAME, AUTHOR_PASSWORD, AUTHOR_EMAIL);
        commenter = new User(COMMENTER_NAME, COMMENTER_PASSWORD, COMMENTER_EMAIL);
        article = new Article(TITLE, CONTENTS, COVER_URL, author);
        comment = new Comment(COMMENT, commenter, article);
    }

    @Test
    public void 특정_게시글에대한_모든_댓글_찾기() {
        // given
        testEntityManager.persist(author);
        testEntityManager.persist(commenter);
        testEntityManager.persist(comment);

        Article persistArticle = testEntityManager.persist(article);

        cleanUpTestEntityManager();

        // then
        assertThat(commentRepository.findByArticle(persistArticle)).isEqualTo(Arrays.asList(comment));
    }

    private void cleanUpTestEntityManager() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}