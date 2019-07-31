package techcourse.myblog.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    private static final String SIGN_UP_PAGE = "/users/signup";
    private static final String USER_NAME_1 = "test1";
    private static final String EMAIL_1 = "test1@test.com";
    private static final String EMAIL_2 = "test2@test.com";
    private static final String PASSWORD_1 = "123456";

    private WebTestClient webTestClient;
    private String cookie;

    @Autowired
    public UserControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @BeforeAll
    void init() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_2)
                        .with("password", PASSWORD_1))
                .exchange();
    }

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange();

        cookie = webTestClient.post().uri("login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }


    @Test
    void 회원가입_페이지_응답_코드_200_테스트() {
        webTestClient.get().uri("/users/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 유저_조회_페이지에_주입한_유저정보가_있는지_확인() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(USER_NAME_1)).isTrue();
                    assertThat(body.contains(EMAIL_1)).isTrue();
                });
    }

    @Test
    void 중복된_이메일_보낼_시_회원가입_페이지로_리다이렉트하는지_확인() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    assertThat(url.contains(SIGN_UP_PAGE)).isTrue();
                });
    }

    @Test
    void 삭제_요청을_보낼_시_3xx_응답_코드인지_확인() {
        webTestClient.delete().uri("/users").header("Cookie", cookie)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/users").header("Cookie", cookie)
                .exchange();
    }
}