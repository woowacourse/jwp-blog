package techcourse.myblog.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import techcourse.myblog.MyblogApplicationTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NoSignInInterceptorTest extends MyblogApplicationTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void 비로그인시_아티클페이지_잘들어가는지() throws Exception {
        mockMvc.perform(get("/articles/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void 비로그인시_articles_writing_접근시_로그인으로_리다이렉트_확인() throws Exception {
        mockMvc.perform(delete("/articles/writing")).andDo(print()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void 비로그인시_article_edit으로_접근시_로그인으로_리다이렉트_확인() throws Exception {
        mockMvc.perform(get("/articles/1/edit")).andDo(print()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void 비로그인시_post_article_로그인으로_리다이렉트_확인() throws Exception {
        mockMvc.perform(post("/articles")).andDo(print()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void 비로그인시_put_article_로그인으로_리다이렉트_확인() throws Exception {
        mockMvc.perform(put("/articles/1")).andDo(print()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void 비로그인시_delete_article_로그인으로_리다이렉트_확인() throws Exception {
        mockMvc.perform(delete("/articles/1")).andDo(print()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void comment생성_비로그인시_로그인으로_리다이렉트_확인() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/comment").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("contents", "abc")
                .param("articleId", "1"))
                .andDo(print())
                .andExpect(status()
                        .isFound())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader("Location").contains("login")).isTrue();
    }

    @Test
    void comment수정페이지_비로그인접근시_로그인으로_리다이렉트_확인() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/comment/1/edit").contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(status()
                        .isFound())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader("Location").contains("login")).isTrue();
    }

    @Test
    void comment수정_비로그인접근시_로그인으로_리다이렉트확신() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/comment/1").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("articleId", String.valueOf(ARTICLE_ID))
                .param("contents", "updatedContents"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader("Location").contains("login")).isTrue();
    }

    @Test
    void comment삭제() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/comment/" + COMMENT_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("articleId", String.valueOf(ARTICLE_ID))
                .param("contents", COMMENT_CONTENTS))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader("Location").contains("login")).isTrue();
    }

}