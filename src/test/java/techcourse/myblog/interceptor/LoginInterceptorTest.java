package techcourse.myblog.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.user.UserDataForTest;
import techcourse.myblog.util.UserUtilForTest;
import techcourse.myblog.util.WebTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginInterceptorTest {
    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        cookie = UserUtilForTest.loginAndGetCookie(webTestClient);
    }

    @Test
    void 로그인_상태일_시_글쓰기_페이지로_이동하는지_테스트() {
        WebTest.executeGetTest(webTestClient, "/writing", cookie)
                .expectStatus().isOk();
    }

    @Test
    void 로그인_상태가_아닐_시_글쓰기_페이지로_이동하는지_테스트() {
        WebTest.executeGetTest(webTestClient, "/writing", UserDataForTest.EMPTY_COOKIE)
                .expectStatus().isFound()
                .expectHeader()
                .valueMatches("location", ".*/login");
    }
}