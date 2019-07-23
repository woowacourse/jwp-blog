package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest {
    static final String testTitle = "Jemok";
    static final String testCoverUrl = "Baegyung";
    static final String testContents = "Naeyong";
    static final Article testArticle = new Article(testTitle, testCoverUrl, testContents);

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
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
                                .param("title", testTitle)
                                .param("coverUrl", testCoverUrl)
                                .param("contents", testContents)
        ).andDo(print())
        .andExpect(status().is3xxRedirection());
        assertThat(articleRepository.count() != 0).isTrue();
        boolean[] isWritten = { false };
        articleRepository.findAll().forEach(article -> {
            isWritten[0] |= article.getTitle().equals(testTitle)
                    & article.getCoverUrl().equals(testCoverUrl)
                    & article.getContents().equals(testContents);
        });
        assertThat(isWritten[0]).isTrue();
    }

    @Test
    void readTest() throws Exception {
        final Article written = articleRepository.save(testArticle);
        mockMvc.perform(get("/articles/0"))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        String body = mockMvc.perform(get("/articles/" + written.getId()))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        assertThat(body.contains(testTitle)).isTrue();
        assertThat(body.contains(testCoverUrl)).isTrue();
        assertThat(body.contains(testContents)).isTrue();
    }

    @Test
    void updateFormTest() throws Exception {
        final Article written = articleRepository.save(testArticle);
        mockMvc.perform(get("/articles/0/edit"))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        final String body = mockMvc.perform(get("/articles/" + written.getId() + "/edit"))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();
        assertThat(body.contains(testTitle)).isTrue();
        assertThat(body.contains(testCoverUrl)).isTrue();
        assertThat(body.contains(testContents)).isTrue();
    }

    @Test
    void updateTest() throws Exception {
        final String updatedTitle = "제목쓰";
        final String updatedCoverUrl = "배경쓰";
        final String updatedContents = "내용쓰";
        final Article written = articleRepository.save(testArticle);
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
        final Article written = articleRepository.save(testArticle);
        mockMvc.perform(delete("/articles/" + written.getId()));
        assertThat(articleRepository.count()).isEqualTo(0L);
    }
}