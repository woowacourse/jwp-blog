package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String testName = "donut";
    private static final String testEmail = "donut@woowa.com";
    private static final String testPassword = "qwer1234";
    private static final User testUser = new User(testName, testEmail, testPassword);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();
        session.setAttribute("email", testEmail);
        session.setAttribute("name", testName);
        userRepository.save(testUser);
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
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void loginTest() throws Exception {
        mockMvc.perform(
                post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("email", testEmail)
                            .param("password", testPassword)
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
        final String newName = "도나쓰";
        final String newEmail = "donatsu@woowa.com";
        final String newPassword = "qwer1234";
        final String wrongName = "도";
        final String wrongEmail = "@";
        final String wrongPassword = "";
        mockMvc.perform(
                post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", newName)
                            .param("email", newEmail)
                            .param("password", newPassword)
        ).andDo(print())
        .andExpect(redirectedUrl("/login"));
        assertThat(userRepository.findByEmail(newEmail).isPresent());
        mockMvc.perform(
                post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", testName)
                            .param("email", testEmail)
                            .param("password", testPassword)
        ).andDo(print())
        .andExpect(redirectedUrl("/signup"));
        mockMvc.perform(
                post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("name", wrongName)
                            .param("email", wrongEmail)
                            .param("password", wrongPassword)
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
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void profileEditFormTest() throws Exception {
        mockMvc.perform(get("/profile/edit").session(session))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/profile/edit"))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void profileEditTest() throws Exception {
        final String newName = "donatsu";
        mockMvc.perform(
                put("/profile/edit").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                    .param("name", testName)
                                    .param("email", "wrongEmail")
                                    .session(session)
        ).andDo(print())
        .andExpect(redirectedUrl("/profile/edit"));
        mockMvc.perform(
                put("/profile/edit").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                    .param("name", newName)
                                    .param("email", testEmail)
                                    .session(session)
        ).andDo(print())
        .andExpect(redirectedUrl("/profile"));
        assertThat(userRepository.findByEmail(testEmail).get().getName().equals(newName));
        mockMvc.perform(
                put("/profile/edit").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                    .param("name", testName)
                                    .param("email", testEmail)
        ).andDo(print())
        .andExpect(redirectedUrl("/login"));

    }

    @Test
    void cancelProfileTest() throws Exception {
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        assertThat(userRepository.findByEmail(testEmail).isPresent());
        mockMvc.perform(delete("/profile").session(session))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
        assertThat(!userRepository.findByEmail(testEmail).isPresent());
    }
}