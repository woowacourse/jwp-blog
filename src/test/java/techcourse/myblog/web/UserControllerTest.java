package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    private static UserDto userDto;

    static {
        userDto = UserDto.builder()
                .userName("Martin")
                .email("martin@gmail.com")
                .password("Aa12345!")
                .confirmPassword("Aa12345!")
                .build();
    }

    @Test
    void show_sign_up() {
        webTestClient.get()
                .uri("/users/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void create_user() {
        postWithBody(webTestClient.post(), "/users/new", userDto)
                .isFound();
    }


    @Test
    void show_login() {
        getWithoutBody("/login").isOk();
    }

    @Test
    void users_withLogin() {
        postWithBody(webTestClient.post(), "/users/new", userDto).isFound();

        loginSession();
        getWithoutBody("/users").isOk();
    }

    @Test
    void users_withoutLogin() {
        postWithBody(webTestClient.post(), "/users/new", userDto).isFound();

        getWithoutBody("/users").isFound();
    }

    @Test
    void check_same_email() {
        postWithBody(webTestClient.post(), "/users/new", userDto).isFound();

        postWithBody(webTestClient.post(), "/users/new", userDto)
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains("중복된 이메일 입니다.")).isTrue();
                });
    }

    @Test
    void check_valid_name() {
        UserDto invalidUser = UserDto.builder()
                .userName("M")
                .email("martin@gmail.com")
                .password("Aa12345!")
                .confirmPassword("Aa12345!")
                .build();

        postWithBody(webTestClient.post(), "/users/new", invalidUser)
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains("형식에 맞는 이름이 아닙니다.")).isTrue();
                });
    }

    @Test
    void check_valid_password() {
        UserDto invalidUser = UserDto.builder()
                .userName("M")
                .email("martin@gmail.com")
                .password("A")
                .confirmPassword("A")
                .build();

        postWithBody(webTestClient.post(), "/users/new", invalidUser)
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains("형식에 맞는 비밀번호가 아닙니다.")).isTrue();
                });
    }

    @Test
    void check_valid_confirm_password() {
        UserDto invalidUser = UserDto.builder()
                .userName("M")
                .email("martin@gmail.com")
                .password("A")
                .confirmPassword("A")
                .build();

        postWithBody(webTestClient.post(), "/users/new", invalidUser)
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains("비밀번호가 일치하지 않습니다.")).isTrue();
                });
    }

    @Test
    void showMyPage_withLogin() {
        loginSession();
        getWithoutBody("/users/mypage").isOk();
    }

    @Test
    void showMyPage_withoutLogin() {
        getWithoutBody("/users/mypage").isFound();
    }

    @Test
    void showUserEdit_withLogin() {
        loginSession();
        getWithoutBody("/users/mypage/edit").isOk();
    }

    @Test
    void showUserEdit_withoutLogin() {
        getWithoutBody("/users/mypage/edit").isFound();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    void loginSession() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            if ("/users".equals(path) || "/users/mypage".equals(path) || "/users/mypage/edit".equals(path)) {
                return exchange.getSession()
                        .doOnNext(webSession ->
                                webSession.getAttributes().put("user", new User("Martin", "martin@gmail.com", "Aa12345!")))
                        .then();
            }
            return null;
        }).build();
    }

    StatusAssertions postWithBody(WebTestClient.RequestBodyUriSpec requestBodyUriSpec, String uri, UserDto userDto) {
        return requestBodyUriSpec
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userDto.getUserName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword())
                        .with("confirmPassword", userDto.getConfirmPassword()))
                .exchange()
                .expectStatus();
    }

    StatusAssertions getWithoutBody(String uri) {
        return webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus();
    }
}
