package techcourse.myblog.web;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.UserControllerTest.testEmail;
import static techcourse.myblog.web.UserControllerTest.testPassword;

public class LogInTestHelper {
    public static String makeLoggedInCookie(WebTestClient webTestClient) {
        ResponseSpec rs = tryLogin(webTestClient, testEmail, testPassword);

        return rs.expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    public static ResponseSpec tryLogin(WebTestClient webTestClient, String email, String password) {
        return webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange();
    }

    public static void assertLoginRedirect(ResponseSpec rs) {
        rs.expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders()
                            .getLocation()
                            .toString()
                            .contains("login")).isTrue(); // 로그아웃에 실패하면 로그인 창으로 간다.
                });
    }
}
