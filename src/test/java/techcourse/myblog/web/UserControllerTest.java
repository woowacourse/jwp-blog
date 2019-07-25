package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String CUSTOM_USER_ID = "1";
    private static final String VALID_NAME = "유효한이름";
    private static final String VALID_EMAIL = "valid@email.com";
    private static final String VALID_PASSWORD = "ValidPassword!123";

    private static char userIdentifier = 'a';

    private BodyInserters.FormInserter<String> validUserData;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        validUserData = getBodyInserters(getValidName(), getValidEmail(), getValidPassword());
    }

    @Test
    public void 회원가입_페이지_테스트() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인이_되어_있는_상태에서_회원가입_페이지로_이동하는_경우_예외처리() {
        // given
        postSignup(validUserData);
        String cookie = getCookie();

        webTestClient.get().uri("/signup")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/.*");
    }

    @Test
    public void 유효한_정보로_회원가입_하는_경우_테스트() {
        // given
        UserDto user = new UserDto();
        user.setName(getValidName());
        user.setEmail(getValidEmail());
        user.setPassword(getValidPassword());

        EntityExchangeResult<byte[]> signupResponse = postSignup(validUserData);

        // when
        webTestClient.get()
                .uri(signupResponse.getResponseHeaders().getLocation())
                .exchange()
                // then
                .expectStatus().isOk();
    }

    @Test
    public void 유효햐지_않은_이름으로_회원가입_하는_경우_예외처리() {
        String invalidName = "1nva1id";

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(invalidName, getValidEmail(), getValidPassword()))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/signup.*");
    }

    @Test
    public void 유효하지_않은_이메일로_회원가입_하는_경우_예외처리() {
        String invalidEmail = "invalidemail";

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(getValidName(), invalidEmail, getValidPassword()))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/signup.*");
    }

    @Test
    public void 유효하지_않은_비밀번호로_회원가입_하는_경우_예외처리() {
        String invalidPassword = "invalidpw";

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(VALID_NAME, VALID_EMAIL, invalidPassword))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/signup.*");
    }

    @Test
    public void 마이페이지가_잘_출력되는지_테스트() {
        // given
        postSignup(validUserData);
        String cookie = getCookie();

        // when
        webTestClient.get().uri("/mypage")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(res -> {
                    // then
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(getValidEmail())).isTrue();
                    assertThat(body.contains(getValidEmail())).isTrue();
                });
    }

    @Test
    public void 로그아웃_상태에서_마이페이지가_잘_출력되는지_테스트() {
        // given
        postSignup(validUserData);

        // when
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/login.*");
    }

    @Test
    public void 회원수정_페이지가_잘_출력되는지_테스트() {
        // given
        postSignup(validUserData);
        String cookie = getCookie();

        // when
        webTestClient.get().uri("/mypage/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(res -> {
                    // then
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(getValidEmail())).isTrue();
                    assertThat(body.contains(getValidEmail())).isTrue();
                });
    }

    @Test
    public void 로그인이_안된_상태에서_회원수정_페이지에_접속하는_경우_예외처리() {
        webTestClient.get()
                .uri("/mypage/edit")
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/login.*");
    }

    @Test
    public void 회원정보_수정_테스트() {
        // given
        String name = "효진쓰";
        String password = "@Password123";

        postSignup(validUserData);
        String cookie = getCookie();

        // then
        webTestClient.put().uri("/mypage/edit")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(name, getValidEmail(), password))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/mypage");
    }

    @Test
    public void 회원조회_페이지가_잘_출력되는지_테스트() {
        // given
        postSignup(validUserData);
        String cookie = getCookie();

        // when
        webTestClient.get().uri("/users")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
                    // then
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(getValidEmail())).isTrue();
                    assertThat(body.contains(getValidEmail())).isTrue();
                }
        );
    }

    @Test
    public void 로그인이_안된_상태에서_회원조회_페이지에_접속하는_경우_예외처리() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    public void 회원탈퇴_테스트() {
        // given
        postSignup(validUserData);
        String cookie = getCookie();

        // when
        webTestClient.delete()
                .uri("/mypage/edit/{userId}", CUSTOM_USER_ID).attribute("", VALID_EMAIL)
                .header("Cookie", cookie)
                .exchange()
                // then
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/.*");
    }

    @Test
    public void 로그아웃_상태에서_회원_탈퇴하는_경우_예외처리() {
        webTestClient.delete()
                .uri("/mypage/edit/{userId}", CUSTOM_USER_ID).attribute("", VALID_EMAIL)
                .exchange()
                // then
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/login.*");
    }

    @AfterEach
    void tearDown() {
        userIdentifier++;
    }

    private String getValidName() {
        return VALID_NAME + userIdentifier;
    }

    private String getValidEmail() {
        return VALID_EMAIL + userIdentifier;
    }

    private String getValidPassword() {
        return VALID_PASSWORD + userIdentifier;
    }

    private String getCookie() {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(validUserData)
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");

    }

    private EntityExchangeResult<byte[]> postSignup(BodyInserters.FormInserter<String> userData) {
        return webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(userData)
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .returnResult()
                ;
    }

    private BodyInserters.FormInserter<String> getBodyInserters(String name, String email, String password) {
        return BodyInserters.fromFormData("userId", CUSTOM_USER_ID)
                .with("name", name)
                .with("email", email)
                .with("password", password);
    }
}