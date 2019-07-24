package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;

import static java.nio.charset.StandardCharsets.UTF_8;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String DEFAULT_EMAIL = "starkim06@naver.com";
    private static final String DEFAULT_PASSWORD = "aA1231!!";

    @Autowired
    private WebTestClient webTestClient;
    private String name;
    private String email;
    private String password;

    @Test
    void 로그인상태_로그인요청() {
        signUp("alswns", DEFAULT_EMAIL, DEFAULT_PASSWORD);
        getAuthorization("/users/login/page")
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/;jsessionid=([0-9A-Z])+")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그아웃상태_로그인요청() {
        signUp("alswns", DEFAULT_EMAIL, DEFAULT_PASSWORD);
        webTestClient.get().uri("/users/login/page")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_테스트() {
        name = "name";
        email = "ILovePobi@naver.com";
        password = "aA1231!@";
        signUp(name, email, password);

        getLogin(email, password)
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/;jsessionid=([0-9A-Z])+")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_실패_테스트() {
        String name = "name";
        String email = "ILovePobi@naver.com";
        String password = "aA1231!@";
        signUp(name, DEFAULT_EMAIL, DEFAULT_PASSWORD);

        getLogin(email, password)
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/err;jsessionid=([0-9A-Z])+")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원등록_테스트() {
        name = "name";
        email = "pobi@naver.com";
        password = "aA1231!@";
        signUp(name, email, password)
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/login")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원등록_실패_테스트() {
        name = "name";
        email = "pobi@naver.com";
        password = "aA123";
        signUp(name, email, password)
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/err;jsessionid=([0-9A-Z])+")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원조회_테스트() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 세션_테스트() {
        signUp("alswns", DEFAULT_EMAIL, DEFAULT_PASSWORD);
        getAuthorization("/users/mypage").expectStatus().isOk();
    }

    @Test
    void 회원등록_오류_테스트() {
        name = "name";
        email = "email";
        password = "password";
        signUp(name, email, password)
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/err;jsessionid=([0-9A-Z])+")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원정보_수정_확인() {
        signUp("alswns", DEFAULT_EMAIL, DEFAULT_PASSWORD);
        getAuthorization("/users/mypage/edit").expectStatus().isOk();
    }

    private WebTestClient.ResponseSpec getAuthorization(String uri) {
        return webTestClient.get().uri(uri)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("starkim06@naver.com:aA1231!!").getBytes(UTF_8)))
                .exchange();
    }

    private WebTestClient.ResponseSpec getLogin(String email, String password) {
        return webTestClient.post().uri("/users/login")
                .body(BodyInserters
                        .fromFormData(PASSWORD, password)
                        .with(EMAIL, email))
                .exchange();
    }

    private WebTestClient.ResponseSpec signUp(String name, String email, String password) {
        return webTestClient.post()
                .uri("/users/join")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData(NAME, name)
                        .with(EMAIL, email)
                        .with(PASSWORD, password))
                .exchange();
    }
}