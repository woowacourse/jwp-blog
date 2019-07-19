package techcourse.myblog.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserService userService;

    static int count = 0;
    String email = "email@google.co.kr";
    String name = "name";
    String password = "P@ssw0rd";

    @Test
    void signupFormTest() {
        webTestClient.get().uri("/users/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void signup_성공_테스트() {
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
    void signup_valid_실패_테스트() {
        String email = "emailgoogle.co.kr";
        String name = "na213123me";
        String password = "123";

        final BodyInserters.FormInserter<String> with = BodyInserters.fromFormData("name", name)
                .with("email", email)
                .with("password", password)
                .with("confirmPassword", password);

        webTestClient.post().uri("/users")
                .body(with)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(UserDto.EMAIL_NOT_MATCH_MESSAGE)).isTrue();
            assertThat(body.contains(UserDto.NAME_NOT_MATCH_MESSAGE)).isTrue();
            assertThat(body.contains(UserDto.PASSWORD_NOT_MATCH_MESSAGE)).isTrue();
        });
    }

    @ParameterizedTest(name = "{index}: {4}")
    @MethodSource("invalidParameters")
    @DisplayName("회원가입 유효성 예외처리")
    void 회원가입_유효성_에러_테스트(String name, String email, String password, String message, String me) {

        final BodyInserters.FormInserter<String> with = BodyInserters.fromFormData("name", name)
                .with("email", email)
                .with("password", password)
                .with("confirmPassword", password);



        webTestClient.post().uri("/users")
                .body(with)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(message)).isTrue();
        });
    }

    static Stream<Arguments> invalidParameters() throws Throwable {
        return Stream.of(
                Arguments.of("name", "asfas", "P@assw0rd", UserDto.EMAIL_NOT_MATCH_MESSAGE, "email 양식"),
                Arguments.of("a", "asdf@asd", "P@assw0rd", UserDto.NAME_NOT_MATCH_MESSAGE, "name 2자 미만"),
                Arguments.of("qwertasdfzp", "asdf@asd", "P@assw0rd", UserDto.NAME_NOT_MATCH_MESSAGE, "name 10자 초과"),
                Arguments.of("12ad", "asdf@asd", "P@assw0rd", UserDto.NAME_NOT_MATCH_MESSAGE, "name 숫자 포함"),
                Arguments.of("name", "asdf@asd", "Passw0rd", UserDto.PASSWORD_NOT_MATCH_MESSAGE, "password 특수문자 제외"),
                Arguments.of("name", "asdf@asd", "P@ssword", UserDto.PASSWORD_NOT_MATCH_MESSAGE, "password 숫자 제외"),
                Arguments.of("name", "asdf@asd", "p@ssw0rd", UserDto.PASSWORD_NOT_MATCH_MESSAGE, "password 대문자 제외"),
                Arguments.of("name", "asdf@asd", "P@SSW0RD", UserDto.PASSWORD_NOT_MATCH_MESSAGE, "password 소문자 제외")
        );
    }

    @Test
    void signup_이메일_중복_테스트() {
        Long id = saveUserWithEmail();

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
        Long id = saveUserWithEmail();

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
        Long id = saveUserWithEmail();

        String email1 = "asdf@google.co.kr";
        String name1 = "asdfsadfasdf";
        String password1 = "!234Qwer";

        UserDto.Register userDto = UserDto.Register.builder()
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
        Long id = saveUserWithEmail();

        webTestClient.get().uri("/users/{id}/edit", id)
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
        Long id = saveUserWithEmail();

        webTestClient.get().uri("/users/{id}", id)
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
        Long id = saveUserWithEmail();
        String changedName = "asdf";


        webTestClient.put().uri("/users/{id}", id)
                .body(BodyInserters.fromFormData("name", changedName))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/users/[0-9]+");
    }

    @Test
    void delete() {
        Long id = saveUserWithEmail();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new UserDto.Response(5L,"asd","asd"));

        webTestClient.delete().uri("/users/{id}", id)
                .exchange()
                .expectStatus().is3xxRedirection()
        .expectHeader().valueMatches("location","/");


    }

    private Long saveUserWithEmail() {
        email += ++count;
        UserDto.Register userDto = UserDto.Register.builder()
                .email(email)
                .name(name)
                .password(password)
                .confirmPassword(password)
                .build();

        return userService.save(userDto);
    }
}
