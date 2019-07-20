package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {
    private WebTestClient webTestClient;
    private String testName = "abcdeFGHIJ";
    private String testPassword = "abcdEFGH123!@#";
    private String testEmail = "abc@hi.com";
    private long defaultId = 1;
    private String defaultName = "default";
    private String defaultPassword = testPassword;
    private String defaultEmail = "default@default.com";


    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AccountControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @BeforeEach
    void setUp() {
        testSignupProcess(defaultName, defaultPassword, defaultEmail);
    }

    @Test
    void showSignupPageTest() {
        webTestClient.get()
                .uri("/accounts/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void processSignupTest() {
        testSignupProcess(testName, testPassword, testEmail)
                .expectStatus()
                .isFound();
    }

    @Test
    void failSignupTest_Name1Word() {
        String wrongName = "a";
        testSignupProcess(wrongName, testPassword, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSignupTest_Name11Word() {
        String wrongName = "abcdefghijk";
        testSignupProcess(wrongName, testPassword, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSignupTest_NameNotNumber() {
        String wrongName = "abcde1a";

        testSignupProcess(wrongName, testPassword, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSignupTest_NameNotSpecialCharacter() {
        String wrongName = "abcde*a";

        testSignupProcess(wrongName, testPassword, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSignupTest_email골뱅이_없음() {
        String wrongEmail = "abcd.d";

        testSignupProcess(testName, testPassword, wrongEmail)
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void failSignupTest_email기본문자열() {
        String wrongEmail = "abcd";

        testSignupProcess(testName, testPassword, wrongEmail)
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void failSignupTest_email골뱅이_앞이_없음() {
        String wrongEmail = "@asdsa";

        testSignupProcess(testName, testPassword, wrongEmail)
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void failSignupTest_email골뱅이_뒤가_없음() {
        String wrongEmail = "abc2@";

        testSignupProcess(testName, testPassword, wrongEmail)
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void failSigupTest_Pw소문자없음() {
        String wrongPw = "EFGH123!@#";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSigupTest_Pw대문자없음() {
        String wrongPw = "abcd123!@#";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSigupTest_Pw숫자없음() {
        String wrongPw = "abcdEFGH!@#";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSigupTest_Pw특수문자없음() {
        String wrongPw = "abcdEFGH123";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSigupTest_Pw7자리() {
        String wrongPw = "abcFG1!";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void signupTest_이메일_중복_확인() {
        String duplEmail = "name@hi.com";
        testSignupProcess(testName, testPassword, duplEmail)
                .expectStatus()
                .isFound();

        testSignupProcess(testName, testPassword, duplEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void signupTest_이메일_중복_확인2() {
        testSignupProcess(testName, testPassword, defaultEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인_테스트() {
        String email = "aa@login.com";
        testSignupProcess(testName, testPassword, email)
                .expectStatus()
                .isFound();

        testSuccessLogin(email, testPassword);
    }

    @Test
    void showLoginPage() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void showLoginPage_로그인_상태일_경우() {
        String cookie = getLoginCookie(defaultEmail, defaultPassword);

        webTestClient.get().uri("/login").header("Cookie",cookie)
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isFalse();
                })
        ;
    }

    @Test
    void 로그인_테스트_실패_아이디_틀림() {
        String wrongEmail = "aa";

        testFailLogin(defaultPassword, wrongEmail);
    }

    @Test
    void 로그인_테스트_실패_비밀번호_틀림() {
        String wrongPW = "aa";

        testFailLogin(wrongPW, defaultEmail);
    }

    @Test
    void 로그아웃_성공() {
        testSuccessLogout();
    }

    @Test
    void 로그아웃_실패_로그인_안했을_경우() {
        testFailLogout();
    }

    @Test
    void 마이페이지_접근() {
        webTestClient.get().uri("/accounts/profile/" + defaultId)
                .exchange()
                .expectStatus()
                .isOk().expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(defaultEmail)).isTrue();
            assertThat(body.contains(defaultName)).isTrue();
        });
    }

    @Test
    void 본인_마이페이지_수정_페이지_접근() {
        String cookie = getLoginCookie(defaultEmail, defaultPassword);

        webTestClient.get().uri("/accounts/profile/edit").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(defaultName)).isTrue();
                    assertThat(body.contains(defaultEmail)).isTrue();
                });
    }

    @Test
    void 미로그인시_본인_마이페이지_수정_페이지_접근() {
        webTestClient.get().uri("/accounts/profile/edit")
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isTrue();
                });
    }

    @Test
    void 마이페이지_수정_후_저장_성공() {
        String cookie = getLoginCookie(defaultEmail, defaultPassword);

        webTestClient.put().uri("/accounts/profile/edit").header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", defaultName + "a")
                        .with("email", defaultEmail)
                        .with("password", defaultPassword)
                        .with("id", String.valueOf(defaultId)))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri("/accounts/profile/edit").header("Cookie", cookie)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(innerResponse -> {
                                String body = new String(innerResponse.getResponseBody());
                                assertThat(body.contains(defaultName + "a")).isTrue();
                                assertThat(body.contains(defaultEmail)).isTrue();
                            });

                    webTestClient.put().uri("/accounts/profile/edit").header("Cookie", cookie)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .body(BodyInserters
                                    .fromFormData("name", defaultName)
                                    .with("email", defaultEmail)
                                    .with("password", defaultPassword)
                                    .with("id", String.valueOf(defaultId)))
                            .exchange()
                            .expectStatus()
                            .isFound();
                });
    }

    @Test
    void showUsersPage() {
        webTestClient.get().uri("/accounts/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(defaultName)).isTrue();
                    assertThat(body.contains(defaultEmail)).isTrue();
                })
        ;
    }

    private String getLoginCookie(String email, String password) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }


    private WebTestClient.ResponseSpec testSignupProcess(String name, String password, String email) {
        return webTestClient.post()
                .uri("/accounts/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange()
                ;
    }

    private WebTestClient.ResponseSpec testSignupProcessWithCookie(String name, String password, String email, String cookie) {
        return webTestClient.post()
                .uri("/accounts/signup").header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange()
                ;
    }

    private void testSuccessLogin(String email, String password) {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isFalse();
                })
        ;
    }

    private void testFailLogin(String wrongPW, String testEmail) {
        String errorMessgae = "아이디나 비밀번호가 잘못되었습니다.";
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", testEmail)
                        .with("password", wrongPW))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(errorMessgae)).isTrue();
        });
        ;
    }

    private void testSuccessLogout() {
        String cookie = getLoginCookie(defaultEmail, defaultPassword);

        webTestClient.get().uri("/logout").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(innerResponse -> {
                    assertThat(innerResponse.getResponseHeaders().getLocation().toString().contains("login")).isFalse();
                });
    }

    private void testFailLogout() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isTrue();
                });
    }
}
