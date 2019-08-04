package techcourse.myblog.interceptor.loggedIn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.web.LoggedInTemplate;

public class LoggedInInterceptorTest extends LoggedInTemplate {
    @BeforeEach
    void setUp() {
        signUpUser();
    }

    @Test
    void 로그인_되어있을때_로그인_페이지_접근() {
        loggedInGetRequest("/login")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/");
    }

    @Test
    void 로그인_되어있을때_회원가입_페이지_접근() {
        loggedInGetRequest("/signup")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/");
    }
}