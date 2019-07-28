package techcourse.myblog.utils;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.LogInInfoDto;

import static techcourse.myblog.service.UserServiceTest.VALID_PASSWORD;

public class Utils {
    private static final String SAMPLE_USER_EMAIL = "test@test.test";
    private static final String SAMPLE_USER_PASSWORD = VALID_PASSWORD;

    public static String logInAsSampleUser(WebTestClient webTestClient) {
        return logIn(webTestClient, new LogInInfoDto(SAMPLE_USER_EMAIL, SAMPLE_USER_PASSWORD));
    }

    public static String logIn(WebTestClient webTestClient, LogInInfoDto logInInfoDto) {
        return webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", logInInfoDto.getEmail())
                        .with("password", logInInfoDto.getPassword()))
                .exchange()
                .returnResult(String.class)
                .getResponseCookies().get("JSESSIONID").get(0).getValue();
    }

}
