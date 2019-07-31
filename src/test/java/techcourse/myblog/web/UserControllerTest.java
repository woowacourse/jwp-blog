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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private String name = "aiden";
    private String email = "aiden@gmail.com";
    private String password = "12WoowaBros@";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();
        session.setAttribute("user", new User(name, email, password));
    }

    @AfterEach
    void tearDown() {
        session.clearAttributes();
        session = null;
    }

    @Test
    void 로그인_폼_테스트() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/login").session(session))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void 회원가입_테스트() throws Exception {
        signUp().andExpect(redirectedUrl("/login"));
    }

    private ResultActions signUp() throws Exception {
        return mockMvc.perform(
                post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", name)
                        .param("email", email)
                        .param("password", password)
        ).andDo(print());
    }

    @Test
    void 로그인_성공_테스트() throws Exception {
        signUp();
        mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("password", password)
        ).andDo(print())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void 로그인_실패_테스트() throws Exception {
        mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "wrong" + email)
                        .param("password", password)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원_조회_테스트() throws Exception {
        mockMvc.perform(get("/users").session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원_정보_조회() throws Exception {
        mockMvc.perform(get("/mypage/edit").session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원_정보_수정() throws Exception {
        signUp();
        mockMvc.perform(
                put("/mypage/edit")
                        .param("name", "whale")
                        .session(session)
        ).andDo(print())
                .andExpect(redirectedUrl("/mypage"));
    }

    @Test
    void 회원_정보_삭제() throws Exception {
        signUp();
        mockMvc.perform(
                delete("/mypage/edit")
                        .session(session)
        ).andDo(print())
                .andExpect(redirectedUrl("/logout"));
    }
}