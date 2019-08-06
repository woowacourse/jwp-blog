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
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.ErrorResponse;
import techcourse.myblog.application.dto.LoginRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.ControllerTestUtil.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginAPIControllerTests {
    private static final String WRONG_PASSWORD = "WrongPassword";

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        // 회원가입
        signUp(webTestClient, NAME, EMAIL, PASSWORD);

    }

    @Test
    void 로그인_실패() {
        LoginRequest request = new LoginRequest(EMAIL, WRONG_PASSWORD);

        EntityExchangeResult<ErrorResponse> response = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), LoginRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ErrorResponse.class)
                .returnResult();

        assertThat(response.getResponseBody().getMessage()).isEqualTo("비밀번호가 일치하지 않습니다!");
    }

    @Test
    void 로그인_성공() {
        LoginRequest request = new LoginRequest(EMAIL, PASSWORD);

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), LoginRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .valueMatches("Location", ".*/")
        ;
    }
}
