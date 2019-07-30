package techcourse.myblog.testutil;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserSaveRequestDto;

public class LoginTestUtil {

    private static UserSaveRequestDto userSaveRequestDto = new UserSaveRequestDto("이름", "test@test.com", "password1!");

    public static void signUp(WebTestClient webTestClient) {
        signUp(webTestClient, userSaveRequestDto);
    }

    public static void signUp(WebTestClient webTestClient, UserSaveRequestDto userSaveRequestDto) {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", userSaveRequestDto.getName())
                        .with("email", userSaveRequestDto.getEmail())
                        .with("password", userSaveRequestDto.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    public static String getJSessionId(WebTestClient webTestClient) {
        return getJSessionId(webTestClient, userSaveRequestDto);
    }

    public static String getJSessionId(WebTestClient webTestClient, UserSaveRequestDto userSaveRequestDto) {
        return webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", userSaveRequestDto.getEmail())
                        .with("password", userSaveRequestDto.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .returnResult(String.class)
                .getResponseCookies()
                .get("JSESSIONID").get(0)
                .getValue();
    }

    public static void deleteUser(WebTestClient webTestClient) {
        webTestClient.delete().uri("/mypage")
                .cookie("JSESSIONID", getJSessionId(webTestClient))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    public static UserSaveRequestDto getUserSaveRequestDto() {
        return userSaveRequestDto;
    }
}
