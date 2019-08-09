package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.UserService;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.domain.UserValidator.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests extends BaseControllerTests {

    @Autowired
    WebTestClient webTestClient;

    private String id;
    private String email;
    private String name;
    private String password;

    @BeforeEach
    void setUp() {
        email = "edlemail@gmail.com";
        name = "name";
        password = "P@ssw0rd";

        UserDto.Register userDto = UserDto.Register.builder()
                .email(email)
                .name(name)
                .password(password)
                .confirmPassword(password)
                .build();

        id = addUser(name, email, password);
    }

    @Test
    @DisplayName("회원가입 Form 요청")
    void signupForm() {
        webTestClient.get().uri("/users/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("회원가입 유효값 입력시 성공")
    void signUpValidInput() {
        UserDto.Register userDto = new UserDto.Register(email + "a", "name", "P@ssw0rd", "P@ssw0rd");

        postUsers(userDto)
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.matches("[0-9]+")).isTrue();
                });
    }

    @Test
    @DisplayName("회원가입 입력값 전부 유효하지 않은 값 입력시 메시지 전체 출력")
    void signup_valid_실패_테스트() {
        UserDto.Register userDto = new UserDto.Register("emailgmlcom", "na213123me", "Pssw0d", "Pssw0d");

        postUsers(userDto)
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(EMAIL_NOT_MATCH_MESSAGE)).isTrue();
                    assertThat(body.contains(NAME_NOT_MATCH_MESSAGE)).isTrue();
                    assertThat(body.contains(PASSWORD_NOT_MATCH_MESSAGE)).isTrue();
                });
    }

    @ParameterizedTest(name = "{index}: {4}")
    @MethodSource("invalidParameters")
    @DisplayName("회원가입 한가지 유효값 입력한 경우 예외처리")
    void 회원가입_유효성_에러_테스트(String name, String email, String password, String message, String me) {
        UserDto.Register userDto = new UserDto.Register(email, name, password, password);

        postUsers(userDto)
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(message)).isTrue();
                });
    }

    static Stream<Arguments> invalidParameters() throws Throwable {
        return Stream.of(
                Arguments.of("name", "asfas", "P@assw0rd", EMAIL_NOT_MATCH_MESSAGE, "email 양식"),
                Arguments.of("a", "asdf@asd", "P@assw0rd", NAME_NOT_MATCH_MESSAGE, "name 2자 미만"),
                Arguments.of("qwertasdfzp", "asdf@asd", "P@assw0rd", NAME_NOT_MATCH_MESSAGE, "name 10자 초과"),
                Arguments.of("12ad", "asdf@asd", "P@assw0rd", NAME_NOT_MATCH_MESSAGE, "name 숫자 포함"),
                Arguments.of("name", "asdf@asd", "Passw0rd", PASSWORD_NOT_MATCH_MESSAGE, "password 특수문자 제외"),
                Arguments.of("name", "asdf@asd", "P@ssword", PASSWORD_NOT_MATCH_MESSAGE, "password 숫자 제외"),
                Arguments.of("name", "asdf@asd", "p@ssw0rd", PASSWORD_NOT_MATCH_MESSAGE, "password 대문자 제외"),
                Arguments.of("name", "asdf@asd", "P@SSW0RD", PASSWORD_NOT_MATCH_MESSAGE, "password 소문자 제외")
        );
    }

    @Test
    void 회원가입_이메일_중복_입력() {
        UserDto.Register userDto = new UserDto.Register(email, name, password, password);

        postUsers(userDto)
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(UserService.EMAIL_DUPLICATE_MESSAGE)).isTrue();
                });
    }

    @Test
    void 회원가입_비밀번호_불일치_테스트() {
        String email = "email@google.co.kr";
        String name = "name";
        String password = "P@ssw0rd";
        String confirmPassword = "P@ssw0rd+1";
        UserDto.Register userDto = new UserDto.Register(email, name, password, confirmPassword);

        postUsers(userDto)
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(UserService.PASSWORD_INVALID_MESSAGE)).isTrue();
                });
    }

    @Test
    @DisplayName("비로그인 상태에서 로그인 form 접근")
    void loginForm() {
        webTestClient.get().uri("/users/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인 상태에서 로그인 form 접근시 차단")
    void invalidLoginForm() {
        webTestClient.get().uri("/users/login")
                .cookie(JSESSIONID, getJSessionId(email, password))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    @DisplayName("비로그인 상태에서 로그인")
    void login() {

        webTestClient.post().uri("/users/login")
                .body(BodyInserters.
                        fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(name)).isTrue();
                });
    }

    @Test
    @DisplayName("로그인 상태에서 로그인 접근시 차단")
    void invalidLogin() {
        webTestClient.post().uri("/users/login")
                .body(BodyInserters.
                        fromFormData("email", email)
                        .with("password", password))
                .cookie(JSESSIONID, getJSessionId(email, password))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    @DisplayName("유저리스트 보기")
    void userList() {

        webTestClient.get().uri("/users")
                .cookie(JSESSIONID, getJSessionId(email, password))
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(name)).isTrue();
                    assertThat(body.contains(email)).isTrue();
                });
    }

    @Test
    void editForm() {

        webTestClient.get().uri("/users/{id}/edit", id)
                .cookie(JSESSIONID, getJSessionId(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(name)).isTrue();
                    assertThat(body.contains(email)).isTrue();
                });
    }

    @Test
    void show() {

        webTestClient.get().uri("/users/{id}", id)
                .cookie(JSESSIONID, getJSessionId(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(name)).isTrue();
                    assertThat(body.contains(email)).isTrue();
                });
    }

    @Test
    void edit() {
        String changedName = "asdf";
        webTestClient.put().uri("/users/{id}", id)
                .cookie(JSESSIONID, getJSessionId(email, password))
                .body(BodyInserters.fromFormData("name", changedName))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/users/" + id);
    }

    private WebTestClient.ResponseSpec postUsers(final UserDto.Register userDto) {
        return webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword())
                        .with("confirmPassword", userDto.getConfirmPassword()))
                .exchange();
    }

    @AfterEach
    void tearDown() {
        deleteUser(id, email, password);
    }
}
