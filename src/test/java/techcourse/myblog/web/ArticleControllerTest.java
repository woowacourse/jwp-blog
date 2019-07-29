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
import techcourse.myblog.domain.*;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ArticleControllerTest {
    private static final String TEST_TITLE = "Jemok";
    private static final String TEST_COVER_URL = "Baegyung";
    private static final String TEST_CONTENTS = "Naeyong";
    private static final Article TEST_ARTICLE = new Article(TEST_TITLE, TEST_COVER_URL, TEST_CONTENTS);
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    private static final String TEST_COMMENT_CONTENT = "ㅎㅇ";


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
        session.setAttribute("email", TEST_EMAIL);
        session.setAttribute("name", TEST_NAME);
        userRepository.save(TEST_USER);
    }

    @AfterEach
    void tearDown() {
        session.clearAttributes();
        session = null;
        userRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @Test
    void indexTest() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void writingFormTest() throws Exception {
        mockMvc.perform(get("/writing"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void writeTest() throws Exception {
        mockMvc.perform(
                post("/articles").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", TEST_TITLE)
                                .param("coverUrl", TEST_COVER_URL)
                                .param("contents", TEST_CONTENTS)
        ).andDo(print())
        .andExpect(status().is3xxRedirection());
        assertThat(articleRepository.count() != 0).isTrue();
        boolean[] isWritten = { false };
        articleRepository.findAll().forEach(article ->
            isWritten[0] |= article.getTitle().equals(TEST_TITLE)
                    & article.getCoverUrl().equals(TEST_COVER_URL)
                    & article.getContents().equals(TEST_CONTENTS)
        );
        assertThat(isWritten[0]).isTrue();
    }

    @Test
    void readTest() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(get("/articles/0"))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        String body = mockMvc.perform(get("/articles/" + written.getId()))
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
    void updateFormTest() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(get("/articles/0/edit"))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        final String body = mockMvc.perform(get("/articles/" + written.getId() + "/edit"))
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
    void updateTest() throws Exception {
        final String updatedTitle = "제목쓰";
        final String updatedCoverUrl = "배경쓰";
        final String updatedContents = "내용쓰";
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(put("/articles/0/"))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        mockMvc.perform(
                put("/articles/" + written.getId()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                    .param("title", updatedTitle)
                                                    .param("coverUrl", updatedCoverUrl)
                                                    .param("contents", updatedContents)
        ).andDo(print())
        .andExpect(status().is3xxRedirection());
        final String body = mockMvc.perform(get("/articles/" + written.getId()))
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
    void deleteTest() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(delete("/articles/" + written.getId()));
        assertThat(articleRepository.count()).isEqualTo(0);
    }

    @Test
    void writeCommentTest() throws Exception {
        final Article written = articleRepository.save(TEST_ARTICLE);
        mockMvc.perform(
                post("/articles/" + written.getId() + "/comment").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                                .param("contents", TEST_COMMENT_CONTENT)
                                                                .session(session)
        ).andDo(print())
        .andExpect(redirectedUrl("/articles/" + written.getId()));
        assertThat(commentRepository.count()).isEqualTo(1);
        articleRepository.deleteAll();
        assertThat(commentRepository.count()).isEqualTo(0);
    }
}