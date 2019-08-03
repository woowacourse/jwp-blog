package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.domain.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommentControllerTest {
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    private static final User TEST_USER_2 = new User(TEST_NAME, TEST_EMAIL + "a", TEST_PASSWORD);
    private static final String TEST_TITLE = "Jemok";
    private static final String TEST_COVER_URL = "Baegyung";
    private static final String TEST_CONTENTS = "Naeyong";
    private static final Article TEST_ARTICLE = new Article(TEST_USER, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS);
    private static final Comment TEST_COMMENT = new Comment(TEST_ARTICLE, TEST_USER, "ㅎㅇㅎㅇ");

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("user", userRepository.save(TEST_USER));
        userRepository.save(TEST_USER_2);
    }

    @AfterEach
    void tearDown() {
        session.clearAttributes();
        session = null;
        userRepository.deleteAll();
    }

    @Test
    void write_post_success() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(
                post("/articles/" + written.getId() + "/comment").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("contents", "ㅎㅇ")
                        .session(session)
        ).andDo(print())
                .andExpect(redirectedUrl("/articles/" + written.getId()));
        assertThat(commentRepository.count()).isEqualTo(1);
    }

    @Test
    void write_postWithoutLogin_fail() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(
                post("/articles/" + written.getId() + "/comment").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("contents", "ㅎㅇ")
        ).andDo(print())
                .andExpect(redirectedUrl("/login"));
        assertThat(commentRepository.count()).isEqualTo(0);
    }

    @Test
    @Transactional
    void update_editedData_success() throws Exception {
        final String updatedContents = "내용쓰";
        final Article written = articleRepository.save(new Article(TEST_USER, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS));
        final Comment comment = commentRepository.save(new Comment(written, TEST_USER, "ㅎㅇㅎㅇ"));
        mockMvc.perform(
                put("/articles/" + written.getId() + "/comment/" + comment.getId()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("contents", updatedContents)
                        .session(session)
        ).andDo(print())
                .andExpect(status().is3xxRedirection());
        assertThat(commentRepository.findById(comment.getId()).get().getContents()).isEqualTo(updatedContents);
    }

    @Test
    void delete_success() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        final Comment comment = commentRepository.save(TEST_COMMENT);
        mockMvc.perform(delete("/articles/" + written.getId() + "/comment/" + comment.getId()).session(session));
        assertThat(commentRepository.findById(comment.getId()).isPresent()).isFalse();
    }

    @Test
    void delete_NotSameAuthor_Fail() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        final Comment comment = commentRepository.save(TEST_COMMENT);
        session.setAttribute("user", TEST_USER_2);
        mockMvc.perform(delete("/articles/" + written.getId() + "/comment/" + comment.getId()).session(session));
        assertThat(commentRepository.findById(comment.getId()).isPresent()).isTrue();
    }
}
