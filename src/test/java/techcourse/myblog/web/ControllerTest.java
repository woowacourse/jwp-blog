package techcourse.myblog.web;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {
    public static final String JSESSIONID = "JSESSIONID";
    public static final String REGEX_SEMI_COLON = ";";
    public static final String REGEX_EQUAL = "=";
    public static final String USER_EMAIL = "aiden@woowa.com";
    public static final String USER_PASSWORD = "12Woowa@@";
    protected String jSessionId;

    public String login(final WebTestClient webTestClient) {
        return login(webTestClient, USER_EMAIL, USER_PASSWORD);
    }

    public String login(final WebTestClient webTestClient, String email, String password) {
        return extractJSessionId(loginResult(webTestClient, email, password));
    }

    public EntityExchangeResult<byte[]> loginResult(final WebTestClient webTestClient, String userEmail, String userPassword) {
        return webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", USER_EMAIL)
                        .with("password", USER_PASSWORD))
                .exchange()
                .expectBody()
                .returnResult()
                ;
    }

    private String extractJSessionId(EntityExchangeResult<byte[]> loginResult) {
        System.out.println(loginResult.getResponseHeaders().get(SET_COOKIE));
        String[] cookies = loginResult.getResponseHeaders().get(SET_COOKIE).stream()
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_SEMI_COLON);
        return Stream.of(cookies)
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_EQUAL)[1]
                ;
    }
}
