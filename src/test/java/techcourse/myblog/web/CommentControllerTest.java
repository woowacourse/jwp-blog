package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import techcourse.myblog.domain.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private String name = "aiden";
    private String email = "aiden@gmail.com";
    private String password = "12Woowa@";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();
        session.setAttribute("user", new User(1L, name, email, password));
    }

    @AfterEach
    void tearDown() {
        session.clearAttributes();
        session = null;
    }

    private ResultActions signUp() throws Exception {
        return mockMvc.perform(
                post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", name)
                        .param("email", email)
                        .param("password", password)
        ).andDo(print());
    }

    private String getArticleId() throws Exception {
        return mockMvc.perform(
                post("/articles").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "title...")
                        .param("coverUrl", "coverUrl...")
                        .param("contents", "contents...")
                        .session(session)
        ).andDo(print())
                .andReturn().getResponse().getRedirectedUrl().split("/")[2];
    }

    private ResultActions createComment(String articleId) throws Exception {
        String url = String.format("/articles/%s/comment", articleId);
        return mockMvc.perform(
                post(url).param("contents", "comment...")
                        .session(session)
        ).andDo(print());
    }

    @Test
    void 코멘트_작성() throws Exception {
        signUp();
        createComment(getArticleId())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void 코멘트_수정() throws Exception {
        signUp();
        createComment(getArticleId());
        mockMvc.perform(
                put("/comment/2").param("contents", "comment...")
                        .session(session)
        ).andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void 코멘트_삭제() throws Exception {
        signUp();
        createComment(getArticleId());
        mockMvc.perform(delete("/comment/1").session(session))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}