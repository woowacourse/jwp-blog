package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import static org.apache.commons.lang3.SystemUtils.USER_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ArticleRepositoryTest {
    private static final String TITLE = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "cover_url";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private ArticleRepository articleRepository;
    private Article article;
    private User user;

    @Autowired
    public ArticleRepositoryTest(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, EMAIL, PASSWORD);
        article = new Article(TITLE, CONTENTS, COVER_URL, user);
        articleRepository.save(article);
    }

    @Test
    void findOne() {
        assertThat(articleRepository.findById(article.getId()).get()).isEqualTo(article);
    }

    @AfterEach
    void tearDown() {
        articleRepository.delete(article);
    }

}
