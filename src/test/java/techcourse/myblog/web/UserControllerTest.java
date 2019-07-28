package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String LOCATION = "LOCATION";
    private static final String DEFAULT_NAME = "CU";
    private static final String DEFAULT_EMAIL = "starkim06@naver.com";
    private static final String DEFAULT_PASSWORD = "aA1231!@";
    private static final String HEADER_LOCATION = LOCATION;
    private static final String SET_COOKIE = "set-cookie";
    private static final String REGEX_SEMI_COLON = ";";
    private static final String REGEX_EQUAL = "=";
    private static final String JSESSIONID = "JSESSIONID";

    @Autowired
    WebTestClient webTestClient;

    @Test
    void 회원등록_오류_테스트() {
        getExchange(NAME, DEFAULT_EMAIL, PASSWORD, NAME, EMAIL, PASSWORD)
                .expectHeader().valueMatches(LOCATION, "http://localhost:[0-9]+/err;jsessionid=([0-9A-Z])+")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원등록_테스트() {
        String email = "pobi@naver.com";
        
        getExchange(DEFAULT_NAME, email, DEFAULT_PASSWORD, NAME, EMAIL, PASSWORD)
                .expectHeader().valueMatches(LOCATION, "http://localhost:[0-9]+/login")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원조회_테스트() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_테스트() {
        getExchange(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD, NAME, EMAIL, PASSWORD);

        webTestClient.post().uri("/login")
                .body(fromFormData(PASSWORD, DEFAULT_PASSWORD)
                        .with(EMAIL, DEFAULT_EMAIL))
                .exchange()
                .expectHeader().valueMatches(LOCATION, "http://localhost:[0-9]+/;jsessionid=([0-9A-Z])+")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_로그인_접근() {
        String jSessionId = getJsessionid(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);

        webTestClient.get().uri("/login")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 마이페이지_테스트() {
        String jSessionId = getJsessionid(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);

        webTestClient.get().uri("/mypage")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원정보_수정_확인() {
        String jSessionId = getJsessionid(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);

        webTestClient.get().uri("/mypage/edit")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그아웃상태_로그인요청() {
        getExchange(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD, NAME, EMAIL, PASSWORD);

        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    private EntityExchangeResult<byte[]> getLoginResult(String email, String password) {
        return webTestClient.post().uri("/login")
                .body(fromFormData(EMAIL, email)
                        .with(PASSWORD, password))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches(HEADER_LOCATION, ".*/.*")
                .expectBody()
                .returnResult();
    }

    private String extractJSessionId(EntityExchangeResult<byte[]> loginResult) {
        String[] cookies = Objects.requireNonNull(loginResult.getResponseHeaders().get(SET_COOKIE)).stream()
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_SEMI_COLON);
        return Stream.of(cookies)
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_EQUAL)[1];
    }

    private String getJsessionid(String name, String email, String password) {
        getExchange(name, email, password, NAME, EMAIL, PASSWORD);
        EntityExchangeResult loginResult = getLoginResult(email, password);
        return extractJSessionId(loginResult);
    }

    private WebTestClient.ResponseSpec getExchange(String userName, String userEmail, String userPassword, String name, String email, String password) {
        return webTestClient.post()
                .uri("/join")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(insertBody(userName, userEmail, userPassword, name, email, password))
                .exchange();
    }

    private BodyInserters.FormInserter<String> insertBody(String userName, String userEmail, String userPassword, String name, String email, String password) {
        return BodyInserters
                .fromFormData(name, userName)
                .with(email, userEmail)
                .with(password, userPassword);
    }
}

