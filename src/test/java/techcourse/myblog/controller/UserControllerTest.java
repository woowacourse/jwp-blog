package techcourse.myblog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import techcourse.myblog.MyblogApplicationTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static techcourse.myblog.controller.UserController.USER_MAPPING_URL;

class UserControllerTest extends MyblogApplicationTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("로그인 창으로 잘 들어가는 지 테스트")
    void loginForm_call_isOk() throws Exception {
        mockMvc.perform(get(USER_MAPPING_URL))
                .andDo(print())
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DisplayName("회원가입 창으로 잘 들어가는 지 테스트")
    void signUpForm_call_isOk() throws Exception {
        mockMvc.perform(get(USER_MAPPING_URL + "/signup"))
                .andDo(print())
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DisplayName("회원가입을 완료하고 리다이렉트가 잘 되는지 테스트")
    void signUp_post_is3xxRedirect() throws Exception {
        getSignUp("kangmin789@naver.com", "kangmin46", "asdASD12!@").andExpect(redirectedUrl(USER_MAPPING_URL));
    }

    @Test
    @DisplayName("회원가입 떄 이메일 잘못입력 에러 테스트")
    void signUp_email_error() throws Exception {
        getSignUp("@naver.com", "kangmin46", "asdASD12!@").andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 떄 이름 잘못입력 에러 테스트")
    void signUp_name_error() throws Exception {
        getSignUp("kangmin789@naver.com", "aaaaaaaaaaaaaaaaaa", "asdASD12!@").andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 떄 비밀번호 잘못입력 에러 테스트")
    void signUp_password_error() throws Exception {
        getSignUp("kangmin789@naver.com", "kangmin46", "1").andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 잘 되는지 테스트")
    void login() throws Exception {
        getSignUp("kangmin789@naver.com", "kangmin46", "asdASD12!@");
        getLogin("kangmin789@naver.com", "asdASD12!@").andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("로그인 이메일 잘못 입력 시 새로고침 테스트")
    void login_email_error() throws Exception {
        getSignUp("kangmin789@naver.com", "kangmin46", "asdASD12!@");
        getLogin("asd@naver.com", "asdASD12!@").andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 비밀번호 잘못 입력 시 새로고침 테스트")
    void login_password_error() throws Exception {
        getSignUp("kangmin789@naver.com", "kangmin46", "asdASD12!@");
        getLogin("asd@naver.com", "asdASD12!@#$").andExpect(status().isOk());
    }


    private ResultActions getLogin(String email, String password) throws Exception {
        return mockMvc.perform(post(USER_MAPPING_URL + "/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andDo(print());
    }

    private ResultActions getSignUp(String email, String name, String password) throws Exception {
        return this.mockMvc.perform(post(USER_MAPPING_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("name", name)
                .param("password", password))
                .andDo(print());
    }
}