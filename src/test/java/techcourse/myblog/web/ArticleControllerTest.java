package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
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

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
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

    private ResultActions creatArticle() throws Exception {
        return mockMvc.perform(
                post("/articles").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "title...")
                        .param("coverUrl", "coverUrl...")
                        .param("contents", "contents...")
                        .session(session)
        ).andDo(print());
    }

    @Test
    void 글쓰기_폼_열기() throws Exception {
        mockMvc.perform(get("/articles").session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_작성() throws Exception {
        signUp();
        creatArticle().andExpect(status().is3xxRedirection());
    }

    @Test
    void 게시글_수정() throws Exception {
        String title = "제목";
        String coverUrl = "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg";
        String contents = "CONTENTS";
		signUp();
        mockMvc.perform(
                put(creatArticle().andReturn().getResponse().getRedirectedUrl())
                        .param("title", title)
                        .param("coverUrl", coverUrl)
                        .param("contents", contents)
                        .session(session)
        ).andDo(print())
                .andExpect(status().is3xxRedirection());
    }

	@Test
	void 게시글_삭제() throws Exception {
		signUp();
    	mockMvc.perform(delete(creatArticle().andReturn().getResponse().getRedirectedUrl()).session(session))
				.andDo(print())
				.andExpect(status().is3xxRedirection());
	}
}
