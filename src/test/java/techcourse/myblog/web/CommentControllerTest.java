package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CommentControllerTest {
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
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
    private User testUser;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("email", TEST_EMAIL);
        session.setAttribute("name", TEST_NAME);
        testUser = userRepository.save(TEST_USER);
    }

    @AfterEach
    void tearDown() {
        session.clearAttributes();
        session = null;
        articleRepository.deleteAll();
    }

    @Test
    void writeCommentTest() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(
                post("/articles/" + written.getId() + "/comment").contentType(MediaType.APPLICATION_JSON_UTF8)
                                                                .content("{ \"contents\" : \"ㅎㅇ\" }")
                                                                .session(session)
        ).andDo(print())
        .andExpect(status().isOk());
        assertThat(commentRepository.count()).isEqualTo(1);
        articleRepository.deleteAll();
        assertThat(commentRepository.count()).isEqualTo(0);
    }

    @Test
    void findCommentTest() throws Exception {
        final Article written = articleRepository.save(new Article(testUser, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS));
        final Comment comment = commentRepository.save(new Comment(written, testUser, "ㅎㅇㅎㅇ"));
        mockMvc.perform(get("/comment/" + comment.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateCommentTest() throws Exception {
        final String updatedContents = "내용쓰";
        final Article written = articleRepository.save(new Article(testUser, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS));
        final Comment comment = commentRepository.save(new Comment(written, testUser, "ㅎㅇㅎㅇ"));
        mockMvc.perform(
                put("/comment/" + comment.getId()).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{ \"contents\": \"" + updatedContents + "\" }")
                        .session(session)
        ).andDo(print());
        final String body = mockMvc.perform(get("/comment/" + comment.getId()))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();
        assertThat(body.contains(updatedContents)).isTrue();
    }

    @Test
    void deleteCommentTest() throws Exception {
        final Article written = articleRepository.save(new Article(testUser, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS));
        final Comment comment = commentRepository.save(new Comment(written, testUser, "ㅎㅇㅎㅇ"));
        mockMvc.perform(delete("/comment/" + comment.getId()).session(session));
        assertThat(commentRepository.findById(comment.getId()).isPresent()).isFalse();
    }
}