package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.ErrorResponse;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.dto.UserEditRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserAPIControllerTests {

    private static final String NAME = "bmo";
    private static final String EMAIL = "bmo@bmo.com";
    private static final String PASSWORD = "Password123!";
    private static final String LONG_NAME = "abcdefghijk";
    private static final String WRONG_NAME = "123123";
    private static final String GOOD_NAME = "iamgood";
    private static final String WRONG_PASSWORD = "WrongPassword";

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        // 회원가입
        signUp(NAME, EMAIL, PASSWORD);

        // 로그인
        cookie = login(EMAIL, PASSWORD);
    }

    private void signUp(String name, String email, String password) {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
        ;
    }

    private String login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), LoginRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }


    @Test
    void myPage_이름_수정_길이_초과_실패() {
        UserEditRequest request = new UserEditRequest();
        request.setName(LONG_NAME);

        EntityExchangeResult<ErrorResponse> response = webTestClient.put().uri("/users/1")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), UserEditRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ErrorResponse.class)
                .returnResult();

        assertThat(response.getResponseBody().getMessage()).isEqualTo("이름은 2~10자로 제한하며 숫자나 특수문자가 포함될 수 없습니다!");
    }

    @Test
    void myPage_이름_수정_포맷_불일치_실패() {
        UserEditRequest request = new UserEditRequest();
        request.setName(WRONG_NAME);

        EntityExchangeResult<ErrorResponse> response = webTestClient.put().uri("/users/1")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), UserEditRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ErrorResponse.class)
                .returnResult();

        assertThat(response.getResponseBody().getMessage()).isEqualTo("이름은 2~10자로 제한하며 숫자나 특수문자가 포함될 수 없습니다!");
    }

    @Test
    void myPage_이름_수정_성공() {
        UserEditRequest request = new UserEditRequest();
        request.setName(GOOD_NAME);

        webTestClient.put().uri("/users/1")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), UserEditRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .valueMatches("Location", ".*/mypage")
                .expectBody()
                .isEmpty();
    }

}
