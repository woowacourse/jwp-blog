package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.utils.Utils;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static techcourse.myblog.utils.BlogBodyContentSpec.assertThatBodyOf;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String USER_NAME = "Utest";
    private static final String EMAIL = "Utest@test.com";
    private static final String PASSWORD = "UpassWord!1";

    private static final String USER_NAME_2 = "UnewTest";
    private static final String EMAIL_2 = "Utest2@test.com";
    private static final String PASSWORD_2 = "UpassWord!2";

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    @BeforeEach
    void setUp() {
        Utils.createUser(webTestClient, new UserDto(USER_NAME, EMAIL, PASSWORD));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL, PASSWORD));
    }

    @Test
    @DisplayName("회원 가입 페이지를 보여준다.")
    void showSignUpPage() {
        webTestClient.get().uri("/users/sign-up")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("새로운 유저를 저장한다.")
    void save() {
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_2)
                        .with("email", EMAIL_2)
                        .with("password", PASSWORD_2))
                .exchange();

        response.expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(redirectedResponse -> {
                    URI location = redirectedResponse.getResponseHeaders().getLocation();
                    assertThat(location != null ? location.getPath() : "/error").contains("/login");
                });
    }

    @Test
    @DisplayName("이메일이 중복되는 유저를 저장하는 경우에 에러 메시지를 보여준다.")
    void saveDuplicateEmailUser() {
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().isOk();

        assertThatBodyOf(response).contains("이미 사용중인 이메일입니다.");
    }

    @Test
    @DisplayName("유저를 수정한다")
    void update() {
        WebTestClient.ResponseSpec response = webTestClient.put().uri("/users")
                .header("Cookie", cookie)
                .body(fromFormData("userName", USER_NAME_2)
                        .with("password", PASSWORD_2)
                        .with("email", EMAIL))
                .exchange()
                .expectStatus().is3xxRedirection();

        String redirectedLocation = Utils.getRedirectedLocationOf(response);
        WebTestClient.ResponseSpec redirectedResponse = webTestClient.get().uri(redirectedLocation)
                .header("Cookie", cookie)
                .exchange();

        assertThatBodyOf(redirectedResponse).contains(USER_NAME_2);
    }

    @Test
    @DisplayName("유저를 삭제한다.")
    void delete() {
        WebTestClient.ResponseSpec response = webTestClient.delete().uri("/users")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().is3xxRedirection();

        String redirectedLocation = Utils.getRedirectedLocationOf(response);
        WebTestClient.ResponseSpec redirectedResponse = webTestClient.get().uri(redirectedLocation)
                .exchange();

        assertThatBodyOf(redirectedResponse).contains("Login");
    }

    @Test
    @DisplayName("로그인되어있지 않은 경우 유저를 삭제하지 못한다.")
    void deleteFailWhenLoggedOut() {
        WebTestClient.ResponseSpec response = webTestClient.delete().uri("/users")
                .exchange()
                .expectStatus().is3xxRedirection();

        String redirectedLocation = Utils.getRedirectedLocationOf(response);
        WebTestClient.ResponseSpec redirectedResponse = webTestClient.get().uri(redirectedLocation)
                .exchange();

        assertThatBodyOf(redirectedResponse).contains("로그인");
    }

    @AfterEach
    void tearDown() {
        Utils.deleteUser(webTestClient, cookie);
    }
}