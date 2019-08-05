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
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {
    private static final String TEST_NAME = "도나쓰";
    private static final String TEST_EMAIL = "testdonut@woowa.com";
    private static final String TEST_PASSWORD = "qwer1234";
    private static final User TEST_USER = new User(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);

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
    }

    @Test
    void loginFormTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/login").session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void loginTest() throws Exception {
        mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("email", TEST_EMAIL)
                            .param("password", TEST_PASSWORD)
        ).andDo(print())
        .andExpect(redirectedUrl("/"));
        mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("email", "wrongEmail")
                            .param("password", "wrongPassword")
        ).andDo(print())
        .andExpect(redirectedUrl("/login"));
    }

    @Test
    void logoutTest() throws Exception {
        mockMvc.perform(get("/logout").session(session))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void userListTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void signupFormTest() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void signupTest() throws Exception {
        final String NEW_NAME = "도나쓰";
        final String NEW_EMAIL = "donatsu@woowa.com";
        final String NEW_PASSWORD = "qwer1234";
        final String WRONG_NAME = "도";
        final String WRONG_EMAIL = "@";
        final String WRONG_PASSWORD = "";
        mockMvc.perform(
                post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", NEW_NAME)
                            .param("email", NEW_EMAIL)
                            .param("password", NEW_PASSWORD)
        ).andDo(print())
        .andExpect(redirectedUrl("/login"));
        assertThat(userRepository.findByEmail(NEW_EMAIL).isPresent()).isTrue();
        mockMvc.perform(
                post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", TEST_NAME)
                            .param("email", TEST_EMAIL)
                            .param("password", TEST_PASSWORD)
        ).andDo(print())
        .andExpect(redirectedUrl("/signup"));
        mockMvc.perform(
                post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", WRONG_NAME)
                            .param("email", WRONG_EMAIL)
                            .param("password", WRONG_PASSWORD)
        ).andDo(print())
                .andExpect(redirectedUrl("/signup"));
    }

    @Test
    void showProfileTest() throws Exception {
        mockMvc.perform(get("/profile").session(session))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void profileEditFormTest() throws Exception {
        mockMvc.perform(get("/profile/edit").session(session))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/profile/edit"))
                .andDo(print())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void profileEditTest() throws Exception {
        final String newName = "donatsu";
        mockMvc.perform(
                put("/profile/edit").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                    .param("name", TEST_NAME)
                                    .param("email", "wrongEmail")
                                    .session(session)
        ).andDo(print())
        .andExpect(redirectedUrl("/profile/edit"));
        mockMvc.perform(
                put("/profile/edit").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                    .param("name", newName)
                                    .param("email", TEST_EMAIL)
                                    .session(session)
        ).andDo(print())
        .andExpect(redirectedUrl("/profile"));
        assertThat(userRepository.findByEmail(TEST_EMAIL).get().getName().equals(newName)).isTrue();
        mockMvc.perform(
                put("/profile/edit").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                    .param("name", TEST_NAME)
                                    .param("email", TEST_EMAIL)
        ).andDo(print())
        .andExpect(redirectedUrl("/login"));
    }

    @Test
    void cancelProfileTest() throws Exception {
        mockMvc.perform(delete("/profile"))
                .andDo(print())
                .andExpect(redirectedUrl("/login"));
        assertThat(userRepository.findByEmail(TEST_EMAIL).isPresent()).isTrue();
        mockMvc.perform(delete("/profile").session(session))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        assertThat(userRepository.findByEmail(TEST_EMAIL).isPresent()).isFalse();
    }
}