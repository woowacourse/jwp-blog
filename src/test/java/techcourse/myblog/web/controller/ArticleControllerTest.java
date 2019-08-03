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
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ArticleControllerTest {
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    private static final String TEST_TITLE = "Jemok";
    private static final String TEST_COVER_URL = "Baegyung";
    private static final String TEST_CONTENTS = "Naeyong";
    private static final Article TEST_ARTICLE = new Article(TEST_USER, TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS);

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("user", TEST_USER);
        userRepository.save(TEST_USER);
    }

    @AfterEach
    void tearDown() {
        session.clearAttributes();
        session = null;
        articleRepository.deleteAll();
    }

    @Test
    void index_get_isOk() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void writingForm_get_isOk() throws Exception {
        mockMvc.perform(get("/writing").session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void write_post_success() throws Exception {
        mockMvc.perform(
                post("/articles").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", TEST_TITLE)
                        .param("coverUrl", TEST_COVER_URL)
                        .param("contents", TEST_CONTENTS)
                        .session(session)
        ).andDo(print())
                .andExpect(status().is3xxRedirection());
        assertThat(articleRepository.count() != 0).isTrue();
    }

    @Test
    void write_postWithoutLogin_fail() throws Exception {
        mockMvc.perform(
                post("/articles").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", TEST_TITLE)
                        .param("coverUrl", TEST_COVER_URL)
                        .param("contents", TEST_CONTENTS)
        ).andDo(print())
                .andExpect(status().is3xxRedirection());
        assertThat(articleRepository.count() != 0).isFalse();
    }

    @Test
    void wrtie_postDataConfirm_true() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(get("/articles/0"))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        String body = mockMvc.perform(get("/articles/" + written.getId()).session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(body.contains(TEST_TITLE)).isTrue();
        assertThat(body.contains(TEST_COVER_URL)).isTrue();
        assertThat(body.contains(TEST_CONTENTS)).isTrue();
    }

    @Test
    void updateForm_getWithCurrentUserData_true() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(get("/articles/0/edit"))
                .andDo(print())
                .andExpect(redirectedUrl("/login"));
        final String body = mockMvc.perform(get("/articles/" + written.getId() + "/edit").session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(body.contains(TEST_TITLE)).isTrue();
        assertThat(body.contains(TEST_COVER_URL)).isTrue();
        assertThat(body.contains(TEST_CONTENTS)).isTrue();
    }

    @Test
    void update_putEditedData_true() throws Exception {
        final String updatedTitle = "제목쓰";
        final String updatedCoverUrl = "배경쓰";
        final String updatedContents = "내용쓰";
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(
                put("/articles/" + written.getId()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", updatedTitle)
                        .param("coverUrl", updatedCoverUrl)
                        .param("contents", updatedContents)
                        .session(session)
        ).andDo(print())
                .andExpect(status().is3xxRedirection());
        final String body = mockMvc.perform(get("/articles/" + written.getId()).session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(body.contains(updatedTitle)).isTrue();
        assertThat(body.contains(updatedCoverUrl)).isTrue();
        assertThat(body.contains(updatedContents)).isTrue();
    }

    @Test
    void delete_success() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(delete("/articles/" + written.getId()).session(session));
        assertThat(articleRepository.findById(written.getId()).isPresent()).isFalse();
    }

    @Test
    void delete_withOutLogin_fail() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(delete("/articles/" + written.getId()));
        assertThat(articleRepository.findById(written.getId()).isPresent()).isTrue();
    }
}