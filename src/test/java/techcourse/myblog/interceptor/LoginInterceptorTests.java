package techcourse.myblog.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static techcourse.myblog.controller.UserController.USER_MAPPING_URL;

public class LoginInterceptorTests extends MyblogApplicationTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private MockHttpSession session;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();
        userDto = new UserDto("kangmin789@naver.com", "kangmin46", "asdASD12!@");
        session.setAttribute("user", userDto);
    }

    @Test
    @DisplayName("로그인 시 singup 페이지 접속 방지")
    void edit_page_redirect_test() throws Exception {
        mockMvc.perform(get(USER_MAPPING_URL + "/signup").session(session))
                .andDo(print()).andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("로그인 시 login 페이지 접속 방지")
    void show_page_redirect_test() throws Exception {
        mockMvc.perform(get(USER_MAPPING_URL).session(session))
                .andDo(print()).andExpect(redirectedUrl("/"));
    }
}
