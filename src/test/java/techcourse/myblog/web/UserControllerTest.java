package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerTest {
    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private static final String NAME = "yusi";
    private static final String EMAIL = "temp@mail.com";
    private static final String PASSWORD = "12345abc";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
//        User loginUser = new User(NAME, EMAIL, PASSWORD);
//        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
//           String path = exchange.getRequest().getURI().getPath();
//           log.info("path : {}", path);
//           if (path.equals("/mypage") || path.equals("/mypage-edit")) {
//               return exchange.getSession()
//                       .doOnNext(webSession ->
//                            webSession.getAttributes().put("userInfo", UserDto.SessionUserInfo.toDto(loginUser)))
//                       .then();
//           }
//           return null;
//        }).build();
        User loginUser = new User(NAME, EMAIL, PASSWORD);

        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입_테스트")
    public void addUserTest() {
        User user = new User(1L, NAME, EMAIL, PASSWORD);
        addUser(user);
    }

    //TODO 메소드 네이밍 고치기
    private void addUser(User user) {
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
        addUser(user);
        User other = new User(2L, "other", EMAIL, PASSWORD);

        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", other.getName())
                        .with("email", other.getEmail())
                        .with("password", other.getPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원_목록_조회() {
        User user = new User(1L, NAME, EMAIL, PASSWORD);
        addUser(user);

        final int count = 1;
        String element = "class=\"card-body\"";

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, element));
                });
    }

    @Test
    public void 로그인_성공() {
        User user = new User(1L, NAME, EMAIL, PASSWORD);
        addUser(user);

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
    public void 로그인_실패_이메일이_없을때() {
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
    public void 로그인_실패_비밀번호가_다를때() {
        User user = new User(1L, NAME, EMAIL, PASSWORD);

        addUser(user);

        webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", EMAIL)
                        .with("name", NAME)
                        .with("password", "12345678")
                )
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그아웃_테스트() {
        로그인_성공();

        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertFalse(body.contains("로그아웃"));
                });
    }
    
    @Test
    public void 마이페이지의_수정_버튼클릭시_수정페이지_이동() {
        User user = new User(NAME, EMAIL, PASSWORD);
        addUser(user);

        User check = userRepository.findByEmail(EMAIL).orElseThrow(NoSuchElementException::new);
        webTestClient.get().uri("/users/edit/" + check.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원_탈퇴() {
        User user = new User(NAME, EMAIL, PASSWORD);
        addUser(user);
        webTestClient.delete().uri("/users/{email}", EMAIL)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertFalse(body.contains("로그아웃"));
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