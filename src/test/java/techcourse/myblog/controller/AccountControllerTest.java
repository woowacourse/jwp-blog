package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.controller.AccountController.ACCOUNT_URL;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest extends MyblogApplicationTests {
    private WebTestClient webTestClient;
    private String testName = "abcdeFGHIJ";
    private String testPassword = "abcdEFGH123!@#";
    private String testEmail = "abc@hi.com";
    private LoginControllerTest loginControllerTest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AccountControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
        this.loginControllerTest = new LoginControllerTest(this.webTestClient, this.userRepository);
    }

    @Test
    void showSignupPageTest() {
        webTestClient.get()
                .uri(ACCOUNT_URL + "/signup")
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
        testSignupProcess(testName, testPassword, USER_EMAIL)
                .expectStatus()
                .isOk();
    }

    @Test
    void 마이페이지_접근() {
        webTestClient.get().uri( ACCOUNT_URL + "/profile/" + USER_ID)
                .exchange()
                .expectStatus()
                .isOk().expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(USER_EMAIL)).isTrue();
            assertThat(body.contains(USER_NAME)).isTrue();
        });
    }

    @Test
    void 본인_마이페이지_수정_페이지_접근() {
        String cookie = loginControllerTest.getLoginCookie(USER_EMAIL, USER_PASSWORD);

        webTestClient.get().uri( ACCOUNT_URL + "/profile/edit").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(USER_NAME)).isTrue();
                    assertThat(body.contains(USER_EMAIL)).isTrue();
                });
    }

    @Test
    void 미로그인시_본인_마이페이지_수정_페이지_접근() {
        webTestClient.get().uri( ACCOUNT_URL +"/profile/edit")
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
        String cookie = loginControllerTest.getLoginCookie(USER_EMAIL, USER_PASSWORD);

        webTestClient.put().uri(ACCOUNT_URL + "/profile/edit").header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", USER_NAME + "a")
                        .with("email", USER_EMAIL)
                        .with("password", USER_PASSWORD)
                        .with("id", String.valueOf(USER_ID)))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(ACCOUNT_URL + "/profile/edit").header("Cookie", cookie)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(innerResponse -> {
                                String body = new String(innerResponse.getResponseBody());
                                assertThat(body.contains(USER_NAME + "a")).isTrue();
                                assertThat(body.contains(USER_EMAIL)).isTrue();
                            });

                    webTestClient.put().uri(ACCOUNT_URL + "/profile/edit").header("Cookie", cookie)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .body(BodyInserters
                                    .fromFormData("name", USER_NAME)
                                    .with("email", USER_EMAIL)
                                    .with("password", USER_PASSWORD)
                                    .with("id", String.valueOf(USER_ID)))
                            .exchange()
                            .expectStatus()
                            .isFound();
                });
    }

    @Test
    void showUsersPage() {
        webTestClient.get().uri(ACCOUNT_URL + "/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(USER_NAME)).isTrue();
                    assertThat(body.contains(USER_EMAIL)).isTrue();
                })
        ;
    }

    @Test
    void deleteUser() {
        String name = "delete";
        String password = "asdfASDF1234!@#$";
        String email = "delete@user.com";

        testSignupProcess(name, password, email);
        String cookie = loginControllerTest.getLoginCookie(email, password);
        webTestClient.delete().uri(ACCOUNT_URL + "/user").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound();

        webTestClient.get().uri(ACCOUNT_URL + "/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(email)).isFalse();
                })
        ;
    }


    protected WebTestClient.ResponseSpec testSignupProcess(String name, String password, String email) {
        return webTestClient.post()
                .uri(ACCOUNT_URL + "/user")
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
                .uri(ACCOUNT_URL + "/user").header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange()
                ;
    }
}
