package techcourse.myblog.util;

import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.user.UserDataForTest;

public class UserUtilForTest {

    public static void signUp(WebTestClient webTestClient) {
        WebTest.executePostTest(webTestClient, "/users", UserDataForTest.EMPTY_COOKIE, UserDataForTest.NEW_USER_BODY);
    }

    public static String loginAndGetCookie(WebTestClient webTestClient) {
        return WebTest.executePostTest(webTestClient, "/login", UserDataForTest.EMPTY_COOKIE,
                UserDataForTest.LOGIN_BODY)
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}