package techcourse.myblog.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.data.UserDataForTest;

public class UserUtilForTest {
    public static void signUp(WebTestClient webTestClient) {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", UserDataForTest.USER_NAME))
                .exchange();
    }

    public static String loginAndGetCookie(WebTestClient webTestClient) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }
}
