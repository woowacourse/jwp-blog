package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleAssembler;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.article.ArticleRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ArticleRepositoryTest {

    private static final String AUTHOR_NAME = "ike";
    private static final String AUTHOR_PASSWORD = "Password1!";
    private static final String AUTHOR_EMAIL = "ike@gmail.com";

    private static final String TITLE = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "coverUrl";

    private User persistUser;
    private Article persistArticle;
    private Article persistArticle1;
    private Article persistArticle2;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        User author = new User(AUTHOR_NAME, AUTHOR_PASSWORD, AUTHOR_EMAIL);
        persistUser = testEntityManager.persist(author);

        Article article = new Article(TITLE, CONTENTS, COVER_URL, author);
        persistArticle = testEntityManager.persist(article);

        Article article1 = new Article("1", "1", "1", author);
        persistArticle1 = testEntityManager.persist(article1);

        Article article2 = new Article("2", "2", "2", author);
        persistArticle2 = testEntityManager.persist(article2);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void 게시글_생성() {
        articleRepository.save(persistArticle);
        assertThat(articleRepository.findById(persistArticle1.getId()).get()).isEqualTo(persistArticle1);
    }

    @Test
    void 특정_게시글_조회_id() {
        assertThat(articleRepository.findById(persistArticle.getId()).get().getTitle()).isEqualTo(TITLE);
        assertThat(articleRepository.findById(persistArticle.getId()).get().getId()).isEqualTo(persistArticle.getId());
        assertThatThrownBy(() -> articleRepository.findById(100L).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_게시글_조회() {
        List<Article> list = new ArrayList<>(articleRepository.findAll());

        assertThat(list.get(0)).isEqualTo(persistArticle);
        assertThat(list.get(1)).isEqualTo(persistArticle1);
        assertThat(list.get(2)).isEqualTo(persistArticle2);
    }

    @Test
    void 게시글_수정() {
        ArticleRequest articleRequest = new ArticleRequest("a100", "b", "c");
        Article article = articleRepository.findById(persistArticle.getId()).orElseThrow(IllegalAccessError::new);
        article.update(ArticleAssembler.toEntity(articleRequest, persistUser));
        articleRepository.save(article);

        assertThat(articleRepository.findById(persistArticle.getId()).get().getTitle()).isEqualTo("a100");
    }

    @Test
    void 게시글_삭제() {
        articleRepository.deleteById(persistArticle.getId());
        assertThatThrownBy(() -> articleRepository.findById(persistArticle.getId()).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}