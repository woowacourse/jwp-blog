package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static techcourse.myblog.presentation.controller.UserController.USER_MAPPING_URL;
import static techcourse.myblog.presentation.controller.UserController.WRONG_LOGIN_VALUE_MESSAGE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private final String DUMMY_EMAIL = "abaacd@abc.com";
    private final String DUMMY_NAME = "abcd";
    private final String DUMMY_PASSWORD = "asdASD12!@";
    private final String WRONG_EMAIL = "wrong@wrong.com";
    private final String WRONG_PASSWORD = "asdASD12!@wrong";
    UserDto userDto;

    @Autowired
    WebTestClient webTestClient;
    private String cookie;


    @BeforeEach
    void setup() {
        signupRequest(DUMMY_EMAIL, DUMMY_NAME, DUMMY_PASSWORD);
        cookie = getCookie(DUMMY_EMAIL, DUMMY_PASSWORD);
    }

    @Test
    void loginForm_get_isOk() {
        webTestClient.get()
                .uri(USER_MAPPING_URL)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void signUpForm_get_isOk() {
        webTestClient.get()
                .uri(USER_MAPPING_URL + "/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void signUp_duplicatedEmail_exception() {
        signupRequest(DUMMY_EMAIL, DUMMY_NAME, DUMMY_PASSWORD)
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertTrue(body.contains("중복된 이메일 입니다."));
                });
    }

    @Test
    void signUp_newID_is3xxRedirect() {
        signupRequest("kangmin7@abc.com", "abc", "asdASD12!@")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void login_wrongId_redirectToMain() {
        loginRequest(WRONG_EMAIL, WRONG_PASSWORD)
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(WRONG_LOGIN_VALUE_MESSAGE));
        });
    }

    @Test
    void login_ifSuccessHaveCookie_true() {
        webTestClient.get().uri("/")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectHeader();
    }

    @Test
    void login_successMainPageHaveName_isOk() {
        webTestClient.get().uri("/").header("cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(DUMMY_NAME)).isTrue();
                });
    }


    @Test
    void login_loginGetEditPage_isOk() {
        webTestClient.get().uri("/user/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectHeader();
    }

    @Test
    void login_notLoginGetEditPage_redirectToLogin() {
        webTestClient.get().uri("/user/edit")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    System.out.println(response.getResponseHeaders());
                    assertThat(url.contains("/user")).isTrue();
                });
    }


    @Test
    void logout_post_is3xxRedirect() {
        userDto = new UserDto("kangmin789@abc.com", "abc", "asdASD12!@");
        commonRequest(POST, userDto, "/logout")
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void logout_mainPageHaveLoginString_() {
        webTestClient.get().uri(USER_MAPPING_URL + "/logout")
                .header("cookie", cookie)
                .exchange();

        webTestClient.get().uri("/").header("cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("Login")).isTrue();
                });

    }

    private WebTestClient.ResponseSpec commonRequest(HttpMethod method, final UserDto userDto, String attatchedUrl) {
        return webTestClient.method(method)
                .uri(USER_MAPPING_URL + attatchedUrl)
                .body(fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword()))
                .exchange();
    }

    private WebTestClient.ResponseSpec commonRequest(HttpMethod method, final UserDto userDto) {
        return commonRequest(method, userDto, "");
    }

    private WebTestClient.ResponseSpec signupRequest(String email, String name, String password) {
        UserDto userDto = UserDto.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        return commonRequest(POST, userDto);
    }

    private WebTestClient.ResponseSpec loginRequest(String email, String password) {
        LoginDto loginDto = LoginDto.builder()
                .email(email)
                .password(password)
                .build();
        return webTestClient.post()
                .uri(USER_MAPPING_URL + "/login")
                .body(fromFormData("email", loginDto.getEmail())
                        .with("password", loginDto.getPassword()))
                .exchange();
    }

    private String getCookie(String email, String password) {
        return loginRequest(email, password)
                .expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}