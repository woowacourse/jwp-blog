package techcourse.myblog.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class ArticleServiceTest {
    private static final String TEST_TITLE = "Jemok";
    private static final String TEST_COVER_URL = "Baegyung";
    private static final String TEST_CONTENTS = "Naeyong";
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    private static final Article TEST_ARTICLE = new Article(TEST_USER, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS);
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleService articleService;

    @BeforeEach
    void setUp() {
        userRepository.save(TEST_USER);

    }

    @Test
    void save_success() {
        articleService.write(TEST_ARTICLE);
        assertThat(articleRepository.findById(TEST_ARTICLE.getId()).get()).isEqualTo(TEST_ARTICLE);
    }

    @Test
    void update_editedTitle_success() {
        Article article = articleService.write(TEST_ARTICLE);
        Article editedArticle = new Article(TEST_USER, "edited", "edited", "edited");
        articleService.tryUpdate(article.getId(), editedArticle);
        assertThat(articleRepository.findById(article.getId()).get().getTitle()).isEqualTo(editedArticle.getTitle());
    }

    @Test
    void update_editedCoverUrl_success() {
        Article article = articleService.write(TEST_ARTICLE);
        Article editedArticle = new Article(TEST_USER, "edited", "edited", "edited");
        articleService.tryUpdate(article.getId(), editedArticle);
        assertThat(articleRepository.findById(article.getId()).get().getCoverUrl()).isEqualTo(editedArticle.getCoverUrl());
    }

    @Test
    void update_editedContents_success() {
        Article article = articleService.write(TEST_ARTICLE);
        Article editedArticle = new Article(TEST_USER, "edited", "edited", "edited");
        articleService.tryUpdate(article.getId(), editedArticle);
        assertThat(articleRepository.findById(article.getId()).get().getContents()).isEqualTo(editedArticle.getContents());
    }

    @Test
    void delete_success() {
        Article article = articleService.write(TEST_ARTICLE);
        articleService.tryDelete(article.getId(), TEST_USER);
        assertThat(articleRepository.findById(article.getId()).isPresent()).isEqualTo(false);
    }

    @Test
    void delete_wrongAuthor_fail() {
        User wrongUser = new User("abc", "wrong@wrong.com", TEST_PASSWORD);
        userRepository.save(wrongUser);
        Article article = articleService.write(TEST_ARTICLE);
        articleService.tryDelete(article.getId(), TEST_USER);
        assertThat(articleRepository.findById(article.getId()).isPresent()).isEqualTo(true);
    }
}