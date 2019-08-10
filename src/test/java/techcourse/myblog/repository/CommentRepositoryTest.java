package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.template.StaticVariableTemplate;
import techcourse.myblog.domain.User;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTest extends StaticVariableTemplate {
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

    @Test
    public void 게시글에_대한_댓글이_달리는지_테스트() {
        // given
        testEntityManager.persist(author);
        testEntityManager.persist(commenter);
        Article persistArticle = testEntityManager.persist(article);

        // when
        persistArticle.addComments(comment);

        cleanUpTestEntityManager();

        // then
        assertThat(commentRepository.findByArticle(persistArticle).size()).isEqualTo(1);
    }

    @Test
    public void 게시글이_삭제되면_댓글도_함께_삭제되는지_테스트() {
        // given
        testEntityManager.persist(author);
        testEntityManager.persist(commenter);
        Article persistArticle = testEntityManager.persist(article);

        persistArticle.addComments(comment);

        // when
        testEntityManager.remove(persistArticle);

        cleanUpTestEntityManager();

        // then
        assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }

    private void cleanUpTestEntityManager() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}