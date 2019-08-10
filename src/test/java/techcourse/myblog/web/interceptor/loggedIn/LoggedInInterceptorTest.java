package techcourse.myblog.web.interceptor.loggedIn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.template.LoginTemplate;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoggedInInterceptorTest extends LoginTemplate {
    @BeforeEach
    void setUp() {
        registeredWebTestClient();
    }

    @Test
    void 로그인_되어있을때_로그인_페이지_접근() {
        loggedInGetRequest("login")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/");
    }

    @Test
    void 로그인_되어있을때_회원가입_페이지_접근() {
        loggedInGetRequest("signup")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/");
    }
}