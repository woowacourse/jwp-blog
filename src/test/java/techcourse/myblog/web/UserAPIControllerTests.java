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
import techcourse.myblog.application.dto.UserEditRequest;
import techcourse.myblog.web.dto.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.ControllerTestUtil.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserAPIControllerTests {
    private static final String LONG_NAME = "abcdefghijk";
    private static final String WRONG_NAME = "123123";
    private static final String GOOD_NAME = "iamgood";

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        // 회원가입
        signUp(webTestClient, NAME, EMAIL, PASSWORD);

        // 로그인
        cookie = login(webTestClient, EMAIL, PASSWORD);
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
