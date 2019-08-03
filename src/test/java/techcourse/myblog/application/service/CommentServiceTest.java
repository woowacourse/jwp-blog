package techcourse.myblog.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.domain.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class CommentServiceTest {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentService commentService;
    private static final String TEST_TITLE = "Jemok";
    private static final String TEST_COVER_URL = "Baegyung";
    private static final String TEST_CONTENTS = "Naeyong";
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    private static final Article TEST_ARTICLE = new Article(TEST_USER, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS);
    @BeforeEach
    void setUp() {
        userRepository.save(TEST_USER);
        articleRepository.save(TEST_ARTICLE);
    }
    @Test
    void save_success() {
        String commentContents = "abc";
        Comment comment = commentService.write(TEST_ARTICLE, TEST_USER,commentContents);
        assertThat(commentRepository.findById(comment.getId()).get().getContents()).isEqualTo(commentContents);
    }

    @Test
    void delete_success() {
        Comment comment = commentService.write(TEST_ARTICLE, TEST_USER,"cde");
        commentService.tryDelete(comment.getId(),TEST_USER);
        assertThat(commentRepository.findById(comment.getId()).isPresent()).isEqualTo(false);
    }

    @Test
    void delete_wrongAuthor_fail() {
        User wrongUser = new User("abc", "wrong@wrong.com", TEST_PASSWORD);
        userRepository.save(wrongUser);
        Comment comment = commentService.write(TEST_ARTICLE, wrongUser,"cde");
        commentService.tryDelete(comment.getId(),TEST_USER);
        assertThat(commentRepository.findById(comment.getId()).isPresent()).isEqualTo(true);
    }
}