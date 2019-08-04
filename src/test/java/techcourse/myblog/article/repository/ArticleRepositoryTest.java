package techcourse.myblog.article.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.template.StaticVariableTemplate;
import techcourse.myblog.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ArticleRepositoryTest extends StaticVariableTemplate {
    private User author;
    private Article article;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        author = new User(AUTHOR_NAME, AUTHOR_PASSWORD, AUTHOR_EMAIL);
        article = new Article(TITLE, CONTENTS, COVER_URL, author);
    }

    @Test
    public void 게시글에_작성자가_잘_저장되는지_테스트() {
        // given
        User persistUser = testEntityManager.persist(author);
        Article persistArticle = testEntityManager.persist(article);

        cleanUpTestEntityManager();

        // when
        Optional<Article> actualArticle = articleRepository.findById(persistArticle.getId());

        // then
        actualArticle.ifPresent(a -> assertThat(a.getAuthor()).isEqualTo(persistUser));
    }

    /*@Test
    public void 게시글이_삭제되면_댓글도_함께_삭제되는지_테스트() {
        // given
        testEntityManager.persist(author);
        testEntityManager.persist(commenter);
        Article persistArticle = testEntityManager.persist(article);

        // when
        persistArticle.addComments(comment);

        cleanUpTestEntityManager();

        articleRepository.deleteById(persistArticle.getId());

        // then
        articleRepository.findById(persistArticle.getId()).ifPresent(
                a -> assertThat(a.getComments().size()).isEqualTo(0)
        );
    }*/

    private void cleanUpTestEntityManager() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}