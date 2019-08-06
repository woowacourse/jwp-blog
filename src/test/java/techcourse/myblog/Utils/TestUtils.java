package techcourse.myblog.Utils;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static techcourse.myblog.Utils.TestConstants.*;

public class TestUtils {
    public static String logIn(WebTestClient webTestClient, String email, String password) {
        return webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class)
                .getResponseCookies().get("JSESSIONID").get(0).getValue();
    }

    public static String logInAsBaseUser(WebTestClient webTestClient) {
        return logIn(webTestClient, BASE_USER_EMAIL, BASE_USER_PASSWORD);
    }

    public static String logInAsMismatchUser(WebTestClient webTestClient) {
        return logIn(webTestClient, MISMATCH_USER_EMAIL, MISMATCH_USER_PASSWORD);
    }

    public static String getBody(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec.expectBody(String.class)
                .returnResult()
                .getResponseBody();
    }
}
