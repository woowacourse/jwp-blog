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
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    private static UserSignUpRequestDto userSignUpRequestDto;

    static {
        userSignUpRequestDto = UserSignUpRequestDto.builder()
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
        postWithBody(webTestClient.post(), "/users/new", userSignUpRequestDto)
                .isFound();
    }


    @Test
    void show_login() {
        getWithoutBody("/login").isOk();
    }

    @Test
    void users_withLogin() {
        postWithBody(webTestClient.post(), "/users/new", userSignUpRequestDto).isFound();

        loginSession();
        getWithoutBody("/users").isOk();
    }

    @Test
    void users_withoutLogin() {
        postWithBody(webTestClient.post(), "/users/new", userSignUpRequestDto).isFound();

        getWithoutBody("/users").isFound();
    }

    @Test
    void check_same_email() {
        postWithBody(webTestClient.post(), "/users/new", userSignUpRequestDto).isFound();

        postWithBody(webTestClient.post(), "/users/new", userSignUpRequestDto)
                .isOk()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains("중복된 이메일 입니다.")).isTrue();
                });
    }

    @Test
    void check_valid_name() {
        UserSignUpRequestDto invalidUser = UserSignUpRequestDto.builder()
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
        UserSignUpRequestDto invalidUser = UserSignUpRequestDto.builder()
                .userName("Martin")
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
        UserSignUpRequestDto invalidUser = UserSignUpRequestDto.builder()
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
                                webSession.getAttributes().put(LoginUtil.USER_SESSION_KEY, new User("Martin", "martin@gmail.com", "Aa12345!")))
                        .then();
            }
            return null;
        }).build();
    }

    StatusAssertions postWithBody(WebTestClient.RequestBodyUriSpec requestBodyUriSpec, String uri, UserSignUpRequestDto userSignUpRequestDto) {
        return requestBodyUriSpec
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userSignUpRequestDto.getUserName())
                        .with("email", userSignUpRequestDto.getEmail())
                        .with("password", userSignUpRequestDto.getPassword())
                        .with("confirmPassword", userSignUpRequestDto.getConfirmPassword()))
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
