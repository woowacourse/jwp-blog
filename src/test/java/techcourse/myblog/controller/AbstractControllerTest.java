package techcourse.myblog.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;

import java.util.stream.Stream;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractControllerTest {
    private static final String REGEX_SEMI_COLON = ";";
    private static final String REGEX_EQUAL = "=";
    static final String JSESSIONID = "JSESSIONID";

    User user1 = new User("cony", "cony@cony.com", "@Password12");
    User user2 = new User("buddy", "buddy@buddy.com", "@Password12");

    @Autowired
    private WebTestClient webTestClient;

    EntityExchangeResult<byte[]> login(User user) {
        return webTestClient.post().uri("/login")
                .body(fromFormData("email", user.getEmail())
                        .with("password", user.getPassword()))
                .exchange()
                .expectBody()
                .returnResult();
    }

    String extractJSessionId(EntityExchangeResult<byte[]> loginResult) {
        String[] cookies = loginResult.getResponseHeaders().get("set-Cookie").stream()
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("jSessionId가 없습니다."))
                .split(REGEX_SEMI_COLON);
        return Stream.of(cookies)
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("jSessionId가 없습니다."))
                .split(REGEX_EQUAL)[1];
    }
}
