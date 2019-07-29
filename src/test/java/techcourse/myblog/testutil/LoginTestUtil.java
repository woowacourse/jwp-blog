package techcourse.myblog.testutil;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserSaveParams;

public class LoginTestUtil {

    private static UserSaveParams userSaveParams = new UserSaveParams("이름", "test@test.com", "password1!");

    public static void signUp(WebTestClient webTestClient) {
        signUp(webTestClient, userSaveParams);
    }

    public static void signUp(WebTestClient webTestClient, UserSaveParams userSaveParams) {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", userSaveParams.getName())
                        .with("email", userSaveParams.getEmail())
                        .with("password", userSaveParams.getPassword()))
                .exchange();
    }

    public static String getJSessionId(WebTestClient webTestClient) {
        return getJSessionId(webTestClient, userSaveParams);
    }

    public static String getJSessionId(WebTestClient webTestClient, UserSaveParams userSaveParams) {
        return webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", userSaveParams.getEmail())
                        .with("password", userSaveParams.getPassword()))
                .exchange()
                .returnResult(String.class)
                .getResponseCookies()
                .get("JSESSIONID").get(0)
                .getValue();
    }

    public static void deleteUser(WebTestClient webTestClient) {
        webTestClient.delete().uri("/mypage")
                .cookie("JSESSIONID", getJSessionId(webTestClient))
                .exchange();
    }

    public static UserSaveParams getUserSaveParams() {
        return userSaveParams;
    }
}
