package techcourse.myblog.web;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserSaveParams;

class LoginTestConfig {

    private static UserSaveParams userSaveParams = new UserSaveParams("이름", "test@test.com", "password1!");

    static void signUp(WebTestClient webTestClient) {
        signUp(webTestClient, userSaveParams);
    }

    static void signUp(WebTestClient webTestClient, UserSaveParams userSaveParams) {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", userSaveParams.getName())
                        .with("email", userSaveParams.getEmail())
                        .with("password", userSaveParams.getPassword()))
                .exchange();
    }

    static String getJSessionId(WebTestClient webTestClient) {
        return getJSessionId(webTestClient, userSaveParams);
    }

    static String getJSessionId(WebTestClient webTestClient, UserSaveParams userSaveParams) {
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

    static void deleteUser(WebTestClient webTestClient) {
        webTestClient.delete().uri("/mypage")
                .cookie("JSESSIONID", getJSessionId(webTestClient))
                .exchange();
    }

    static UserSaveParams getUserSaveParams() {
        return userSaveParams;
    }
}
