package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.AuthController.AUTH_DEFAULT_URL;
import static techcourse.myblog.web.UserControllerTest.회원_등록;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    static String testEmail = "pkch@woowa.com";
    static String testPassword = "!234Qwer";

    @Autowired
    private WebTestClient webTestClient;

    static WebTestClient.ResponseSpec 로그인(WebTestClient webTestClient, String email, String password) {
        return webTestClient.post().uri(AUTH_DEFAULT_URL + "/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("email", email)
                                .with("password", password)
                ).exchange();
    }

    static String 로그인_세션_ID(WebTestClient webTestClient, String email, String password) {
        return 로그인(webTestClient, email, password)
                .returnResult(String.class)
                .getResponseHeaders().getFirst("Set-Cookie");
    }

    @Test
    void 로그인_페이지_접근_테스트() {
        webTestClient.get().uri(AUTH_DEFAULT_URL + "/login")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인_정상_흐름_테스트() {
        UserDto testUserDto = new UserDto();
        testUserDto.setName("pkch");
        testUserDto.setEmail("pkch@woowa.com");
        testUserDto.setPassword("qwerqwer");

        회원_등록(webTestClient, testUserDto);
        로그인(webTestClient, testEmail, testPassword)
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(res -> {
                    String redirectUri = res.getResponseHeaders().getLocation().getPath();
                    assertThat(redirectUri).isEqualTo("/");
                });
    }

    @Test
    void 로그인_비밀번호_오류시_로그인_페이지_이동_테스트() {
        로그인(webTestClient, testEmail, "qwer1234")
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(res -> {
                    String redirectUri = res.getResponseHeaders().getLocation().getPath();
                    assertThat(redirectUri).isEqualTo(AUTH_DEFAULT_URL + "/login");
                });
    }

    @Test
    void 로그인_이메일_오류시_로그인_페이지_이동_테스트() {
        로그인(webTestClient, "park@woowa.com", testPassword)
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(res -> {
                    String redirectUri = res.getResponseHeaders().getLocation().getPath();
                    assertThat(redirectUri).isEqualTo(AUTH_DEFAULT_URL + "/login");
                });
    }
}
