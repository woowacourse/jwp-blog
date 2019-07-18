package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @DisplayName("회원가입_테스트")
    public void addUserTest() {
        User user = new User(1L, NAME, EMAIL, PASSWORD);

        addUser(user, response -> {
            String uri = response.getResponseHeaders().get("Location").get(0);
            assertTrue(uri.contains("/login"));
        });
    }

    private void addUser(final User user, final Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", user.getName())
                        .with("email", user.getEmail())
                        .with("password", user.getPassword()))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(consumer);
    }

    @Test
    public void 이메일_중복일때_가입_테스트() {
        User user = new User(1L, NAME, EMAIL, PASSWORD);

        addUser(user, response -> {
            String uri = response.getResponseHeaders().get("Location").get(0);
            assertTrue(uri.contains("/login"));
        });

        User other = new User(2L, "other", EMAIL, PASSWORD);

        addUser(other, response -> {
            String uri = response.getResponseHeaders().get("Location").get(0);
            assertTrue(uri.contains("/signup"));
        });
    }

    @Test
    public void 회원_목록_조회() {
        User user = new User(1L, NAME, EMAIL, PASSWORD);

        addUser(user, response -> {
            String uri = response.getResponseHeaders().get("Location").get(0);
            assertTrue(uri.contains("/login"));
        });

        final int count = 1;
        String delimiter = "class=\"card-body\"";

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, delimiter));
                });
    }

    @Test
    public void 로그인_성공() {
        User user = new User(1L, NAME, EMAIL, PASSWORD);

        addUser(user, response -> {
            String uri = response.getResponseHeaders().get("Location").get(0);
            assertTrue(uri.contains("/login"));
        });

        webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", user.getEmail())
                        .with("name", user.getName())
                        .with("password", user.getPassword())
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertTrue(body.contains(user.getName()));
                });
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
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    String uri = response.getResponseHeaders().get("Location").get(0);
                    assertThat(uri).contains("/login");
                });
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