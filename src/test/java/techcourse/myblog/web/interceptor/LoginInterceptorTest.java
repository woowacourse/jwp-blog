package techcourse.myblog.web.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import techcourse.myblog.MyblogApplicationTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginInterceptorTest extends MyblogApplicationTests {
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
    void 비로그인시_article_edit으로_접근시_로그인으로_리다이렉트_확인() throws Exception {
        mockMvc.perform(get("/articles/1/edit")).andDo(print()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void 비로그인시_post_article_로그인으로_리다이렉트_확인() throws Exception{
        mockMvc.perform(post("/articles")).andDo(print()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void 비로그인시_put_article_로그인으로_리다이렉트_확인() throws Exception{
        mockMvc.perform(put("/articles/1")).andDo(print()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void 비로그인시_delete_article_로그인으로_리다이렉트_확인() throws Exception{
        mockMvc.perform(delete("/articles/1")).andDo(print()).andExpect(redirectedUrl("/login"));
    }
}