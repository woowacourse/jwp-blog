package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerTest {
    private static final String NAME = "yusi";
    private static final String EMAIL = "test@naver.com";
    private static final String PASSWORD = "12345b@aA";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void addUserTest() {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", "yusi")
                        .with("email", "test@naver.com")
                        .with("password", "12345b@aA"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 이메일_중복_가입_테스트() {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", "other")
                        .with("email", "test@naver.com")
                        .with("password", "12345b@aA"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원조회() {
        addUserTest();
        final int count = 1;

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, "class=\"card-body\""));
                });
    }

    @Test
    public void 로그인() {

        addUserTest();
        webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", EMAIL)
                        .with("name", NAME)
                        .with("password", PASSWORD)
                )
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_실패() {
        webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", EMAIL)
                        .with("name", NAME)
                        .with("password", PASSWORD)
                )
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_페이지_이동() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원가입_페이지_이동() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }
}