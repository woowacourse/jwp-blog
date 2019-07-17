package techcourse.myblog.users;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void signupFormTest() {
        webTestClient.get().uri("/users/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void signup_성공_테스트() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";

        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name",name)
                .with("email",email)
                .with("password",password)
                .with("confirmPassword",password))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.matches("[0-9]+")).isTrue();
        });
    }

    @Test
    void signup_이름_실패_테스트() {
        String email = "emailgoogle.co.kr";
        String name = "na213123me";
        String password = "123";

        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name",name)
                .with("email",email)
                .with("password",password)
                .with("confirmPassword",password))
                .exchange()
                .expectBody().consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(UserDto.EMAIL_NOT_MATCH_MESSAGE)).isTrue();
                    assertThat(body.contains(UserDto.NAME_NOT_MATCH_MESSAGE)).isTrue();
                    assertThat(body.contains(UserDto.PASSWORD_NOT_MATCH_MESSAGE)).isTrue();
        });
    }
}
