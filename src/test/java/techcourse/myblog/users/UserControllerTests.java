package techcourse.myblog.users;

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

    @Autowired
    UserService userService;

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
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", password))
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
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", password))
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(UserDto.EMAIL_NOT_MATCH_MESSAGE)).isTrue();
            assertThat(body.contains(UserDto.NAME_NOT_MATCH_MESSAGE)).isTrue();
            assertThat(body.contains(UserDto.PASSWORD_NOT_MATCH_MESSAGE)).isTrue();
        });
    }

    @Test
    void signup_이메일_중복_테스트() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";
        UserDto userDto = UserDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .confirmPassword(password)
                .build();

        userService.save(userDto);


        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", password))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(UserService.EMAIL_DUPLICATE_MESSAGE)).isTrue();
        });
    }

    @Test
    void 비밀번호_불일치_테스트() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";
        String confirmPassword = "P@ssw0rd+1";

        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", confirmPassword))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(UserService.PASSWORD_INVALID_MESSAGE)).isTrue();
        });
    }

    @Test
    void loginFormTest() {
        webTestClient.get().uri("/users/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void loginTest() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";
        UserDto userDto = UserDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .confirmPassword(password)
                .build();

        userService.save(userDto);

        webTestClient.post().uri("/users/login")
                .body(BodyInserters.fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(name)).isTrue();
        });
    }

    @Test
    void userListTest() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";

        String email1 = "asdf@google.co.kr";
        String name1 = "asdfsadfasdf";
        String password1 = "!234Qwer";

        UserDto userDto = UserDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .confirmPassword(password)
                .build();

        userService.save(userDto);

        userDto = UserDto.builder()
                .email(email1)
                .name(name1)
                .password(password1)
                .confirmPassword(password1)
                .build();

        userService.save(userDto);

        webTestClient.get().uri("/users")
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(name)).isTrue();
            assertThat(body.contains(email)).isTrue();
            assertThat(body.contains(name1)).isTrue();
            assertThat(body.contains(email1)).isTrue();
        });
    }

    @Test
    void editForm() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";
        UserDto userDto = UserDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .confirmPassword(password)
                .build();

        Long userId = userService.save(userDto);

        webTestClient.get().uri("/users/{id}/edit", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(name)).isTrue();
            assertThat(body.contains(email)).isTrue();
        });
    }

    @Test
    void show() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";
        UserDto userDto = UserDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .confirmPassword(password)
                .build();

        Long userId = userService.save(userDto);

        webTestClient.get().uri("/users/{id}", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(name)).isTrue();
            assertThat(body.contains(email)).isTrue();
        });
    }

    @Test
    void edit() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";
        UserDto userDto = UserDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .confirmPassword(password)
                .build();

        Long userId = userService.save(userDto);
        String changedName = "asdf";

        webTestClient.put().uri("/users/{id}", userId)
                .body(BodyInserters.fromFormData("name", changedName))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location",".*/users/[0-9]+");
    }
}
